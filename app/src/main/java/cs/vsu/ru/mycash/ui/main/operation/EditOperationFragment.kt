package cs.vsu.ru.mycash.ui.main.operation

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import cs.vsu.ru.mycash.api.ApiClient
import cs.vsu.ru.mycash.api.ApiService
import cs.vsu.ru.mycash.data.*
import cs.vsu.ru.mycash.databinding.FragmentEditOperationBinding
import cs.vsu.ru.mycash.utils.AppPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import kotlin.streams.toList

class EditOperationFragment : Fragment(),
    DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private var _binding: FragmentEditOperationBinding? = null
    private var cal = Calendar.getInstance()
    private val binding get() = _binding!!
    private lateinit var apiService: ApiService
    private lateinit var appPrefs: AppPreferences
    private val editOperationViewModel: EditOperationViewModel by activityViewModels()
    private val pickImage = 100

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        appPrefs = activity?.let { AppPreferences(it) }!!
        _binding = FragmentEditOperationBinding.inflate(inflater, container, false)

        val operation = editOperationViewModel.operation.value
        if (operation != null)
        {
            binding.editTextSum.setText(operation.value.toString())
            binding.comment.setText(operation.comment.toString())
        }

        Log.e("operation edit", operation.toString())

        getAccounts()
        getCategories()

        if (editOperationViewModel.mode.value == EditOperationViewModel.Mode.INCOME)
        {
            binding.incomeBtn.isEnabled = false
            binding.spendingButton.isEnabled = true
        }
        else {
            binding.incomeBtn.isEnabled = true
            binding.spendingButton.isEnabled = false
        }


        binding.spendingButton.setOnClickListener {
            editOperationViewModel.setMode(EditOperationViewModel.Mode.EXPENSES)
        }
        binding.incomeBtn.setOnClickListener {
            editOperationViewModel.setMode(EditOperationViewModel.Mode.INCOME)
        }

        pickDate()

        binding.saveButton.setOnClickListener {
            if (operation != null)
            {
                if (updateOperation(operation)) {
                    findNavController().navigateUp()
                }
            }

        }

        binding.deleteButton.setOnClickListener {
            if (operation != null)
            {
                deleteOperation(operation)
                findNavController().navigateUp()
            }

        }

        binding.photoBtn.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)

        }

        binding.deletePhoto.setOnClickListener {
            binding.imageView.setImageURI(null)
            binding.deletePhoto.isVisible = false
        }

        editOperationViewModel.imageUri.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.deletePhoto.isVisible = true
                binding.imageView.setImageURI(it)
            } else {
                binding.deletePhoto.isVisible = false
            }
        }

        editOperationViewModel.date.observe(viewLifecycleOwner) {
            val current = Calendar.getInstance()
            val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
            val sdf1 = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
            if (sdf1.format(it.time).equals(sdf1.format(current.time))) {

                val text = "Сегодня, " + sdf.format(it.time)
                binding.textViewDay.text = text
            } else {
                binding.textViewDay.text = sdf.format(it.time)
            }
        }

        editOperationViewModel.mode.observe(viewLifecycleOwner) {
            if (it == EditOperationViewModel.Mode.INCOME) {
                binding.incomeBtn.isEnabled = false
                binding.spendingButton.isEnabled = true
            } else {
                binding.incomeBtn.isEnabled = true
                binding.spendingButton.isEnabled = false
            }
            configureSpinnerCategories()
        }
        return binding.root
    }

    private fun configureSpinnerCategories() {
        if (editOperationViewModel.categories.value != null) {
            var categoriesNames: ArrayList<String> = ArrayList()
            val all = editOperationViewModel.categories.value
            if (editOperationViewModel.mode.value == EditOperationViewModel.Mode.INCOME) {
                val income = all?.filter { it.type == CategoryType.INCOME }
                if (income != null) {
                    categoriesNames = income.stream()
                        .map { it.name }.toList() as ArrayList<String>
                }
            } else if (editOperationViewModel.mode.value == EditOperationViewModel.Mode.EXPENSES) {

                val expenses = all?.filter { it.type == CategoryType.EXPENSE }
                if (expenses != null) {
                    categoriesNames = expenses.stream()
                        .map { it.name }.toList() as ArrayList<String>
                }
            }

            val spinnerCategories = binding.spinnerCategories
            val adapterCategory = ArrayAdapter(
                requireContext(), android.R.layout.simple_spinner_item, categoriesNames
            )
            adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerCategories.adapter = adapterCategory
            val i = categoriesNames.indexOf(editOperationViewModel.categoryName.value)
            spinnerCategories.setSelection(i)

            spinnerCategories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    editOperationViewModel.setCategoryName(categoriesNames[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        }

    }

    private fun configureSpinnerAccounts() {
        if (editOperationViewModel.accounts.value != null) {
            val accountsNames = editOperationViewModel.accounts.value!!
                .stream()
                .map { it.name }.toList() as ArrayList<String>
            val spinnerAccounts = binding.spinnerAccounts

            val adapterAcc = ArrayAdapter(
                requireContext(), android.R.layout.simple_spinner_item, accountsNames
            )
            adapterAcc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerAccounts.adapter = adapterAcc
            val i = accountsNames.indexOf(editOperationViewModel.accountName.value)
            spinnerAccounts.setSelection(i)

            spinnerAccounts.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    editOperationViewModel.setAccountName(accountsNames[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateOperation(operation: Operation): Boolean {
        val categoryName = editOperationViewModel.categoryName.value
        val category = editOperationViewModel.categories.value?.find { it.name == categoryName }
        val accountName = editOperationViewModel.accountName.value
        val value = binding.editTextSum.text

        val cal = editOperationViewModel.date.value
        val comment = binding.comment.text.toString()
        var check = true
        if (category != null && categoryName != null && categoryName.trim().isNotEmpty()
            && accountName != null && accountName.trim().isNotEmpty()
            && value != null && value.trim().isNotEmpty()
            && cal != null
        ) {
            val datetime: LocalDateTime = LocalDateTime.ofInstant(
                cal.toInstant(), cal.timeZone.toZoneId()
            )

            operation.category = category
            operation.accountName = accountName
            operation.value = value.toString().toDouble()
            operation.dateTime = datetime.toString()
            operation.comment = comment

            apiService = ApiClient.getClient(appPrefs.token.toString())

            apiService.addOperation(operation).enqueue(object : Callback<OperationResponse> {
                override fun onResponse(
                    call: Call<OperationResponse>,
                    response: Response<OperationResponse>
                ) {
                    if (response.body() != null) {
                        if (response.body()!!.type == LimitType.CATEGORY) {
                            Toast.makeText(
                                context,
                                "Вы превысили лимит по категории!",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (response.body()!!.type == LimitType.ACCOUNT) {
                            Toast.makeText(
                                context,
                                "Вы превысили лимит по счету!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        check = true
                    } else {

                        check = false
                    }

                }

                override fun onFailure(call: Call<OperationResponse>, t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }

            })

        } else if (value.trim().isEmpty()) {
            check = false
            binding.editTextSum.error = "Введите сумму"
        }

        return check
    }

    private fun deleteOperation(operation: Operation) : Boolean
    {
        var check = true
        apiService = ApiClient.getClient(appPrefs.token.toString())
        if (operation.id != null)
        {
            apiService.deleteOperation(operation.id).enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    check = true
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                    check = false
                }
            })
        }
        else {
            check = false
        }

        return check
    }

    private fun getAccounts() {
        apiService = ApiClient.getClient(appPrefs.token.toString())
        apiService.getAccounts().enqueue(object : Callback<List<Account>> {
            override fun onResponse(call: Call<List<Account>>, response: Response<List<Account>>) {
                val accounts = response.body()
                if (accounts != null) {
                    editOperationViewModel.setAccounts(accounts)
                    configureSpinnerAccounts()
                }

            }

            override fun onFailure(call: Call<List<Account>>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun getCategories() {
        apiService = ApiClient.getClient(appPrefs.token.toString())
        apiService.getCategories().enqueue(object : Callback<List<Category>> {
            override fun onResponse(
                call: Call<List<Category>>,
                response: Response<List<Category>>
            ) {
                val categories = response.body()
                Log.d("categories response", response.body().toString())
                if (categories != null) {
                    editOperationViewModel.setCategories(categories)
                    configureSpinnerCategories()
                }
            }

            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun pickDate() {
        binding.dateButton.setOnClickListener {
            context?.let { it1 ->
                DatePickerDialog(
                    it1, this,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        }
    }

    override fun onDateSet(p0: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, monthOfYear)
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        TimePickerDialog(
            context, this,
            cal.get(Calendar.HOUR),
            cal.get(Calendar.MINUTE), true
        ).show()
    }

    override fun onTimeSet(p0: TimePicker?, hour: Int, minute: Int) {
        cal.set(Calendar.HOUR, hour)
        cal.set(Calendar.MINUTE, minute)
        editOperationViewModel.setDate(cal)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == pickImage) {
            editOperationViewModel.setImageUri(data?.data)
            val imageUri = editOperationViewModel.imageUri.value
            if (imageUri != null) {
                binding.deletePhoto.isVisible = true
                binding.imageView.setImageURI(imageUri)
            }
        }
    }

}