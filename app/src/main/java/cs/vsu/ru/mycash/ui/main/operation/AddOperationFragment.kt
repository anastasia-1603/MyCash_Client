package cs.vsu.ru.mycash.ui.main.operation

import android.app.Activity.RESULT_OK
import android.app.AlertDialog
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
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.yandex.metrica.impl.ob.re
import cs.vsu.ru.mycash.R
import cs.vsu.ru.mycash.api.ApiClient
import cs.vsu.ru.mycash.api.ApiService
import cs.vsu.ru.mycash.data.*
import cs.vsu.ru.mycash.databinding.FragmentAddOperationBinding
import cs.vsu.ru.mycash.utils.AppPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import kotlin.streams.toList

class AddOperationFragment : Fragment(),
    DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private var _binding: FragmentAddOperationBinding? = null
    private var cal = Calendar.getInstance()
    private val binding get() = _binding!!
    private lateinit var apiService: ApiService
    private lateinit var appPrefs: AppPreferences
    private lateinit var addOperationViewModel: AddOperationViewModel
    private val myactivity = activity
    private val pickImage = 100

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        addOperationViewModel = ViewModelProvider(this)[AddOperationViewModel::class.java]
        appPrefs = activity?.let { AppPreferences(it) }!!
        Log.e("myactivity", myactivity.toString())
        _binding = FragmentAddOperationBinding.inflate(inflater, container, false)
        getAccounts()
        getCategories()

        binding.incomeBtn.isEnabled = false
        binding.spendingButton.isEnabled = true

        binding.spendingButton.setOnClickListener {
            addOperationViewModel.setMode(AddOperationViewModel.Mode.EXPENSES)
        }
        binding.incomeBtn.setOnClickListener {
            addOperationViewModel.setMode(AddOperationViewModel.Mode.INCOME)
        }

        pickDate()

        binding.saveButton.setOnClickListener {
            postOperation()
        }

        binding.cancelButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.photoBtn.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)

        }

        binding.deletePhoto.setOnClickListener {
            binding.imageView.setImageURI(null)
            binding.deletePhoto.isVisible = false
        }

        addOperationViewModel.imageUri.observe(viewLifecycleOwner) {

            if (it != null) {
                binding.deletePhoto.isVisible = true
                binding.imageView.setImageURI(it)
            } else {
                binding.deletePhoto.isVisible = false
            }
        }

        addOperationViewModel.date.observe(viewLifecycleOwner) {
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

        addOperationViewModel.mode.observe(viewLifecycleOwner) {
            if (it == AddOperationViewModel.Mode.INCOME) {
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
        if (addOperationViewModel.categories.value != null) {
            var categoriesNames: ArrayList<String> = ArrayList()
            val all = addOperationViewModel.categories.value
            if (addOperationViewModel.mode.value == AddOperationViewModel.Mode.INCOME) {
                val income = all?.filter { it.type == CategoryType.INCOME }
                if (income != null) {
                    categoriesNames = income.stream()
                        .map { it.name }.toList() as ArrayList<String>
                }
            } else if (addOperationViewModel.mode.value == AddOperationViewModel.Mode.EXPENSES) {

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

            spinnerCategories.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    addOperationViewModel.setCategoryName(categoriesNames[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        }

    }

    private fun configureSpinnerAccounts() {
        if (addOperationViewModel.accounts.value != null) {
            val accountsNames = addOperationViewModel.accounts.value!!
                .stream()
                .map { it.name }.toList() as ArrayList<String>
            val spinnerAccounts = binding.spinnerAccounts

            val adapterAcc = ArrayAdapter(
                requireContext(), android.R.layout.simple_spinner_item, accountsNames
            )
            adapterAcc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerAccounts.adapter = adapterAcc

            spinnerAccounts.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    addOperationViewModel.setAccountName(accountsNames[position])
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun postOperation(): Boolean {
        val categoryName = addOperationViewModel.categoryName.value
        val category = addOperationViewModel.categories.value?.find { it.name == categoryName }
        val accountName = addOperationViewModel.accountName.value
        val value = binding.editTextSum.text

        val type = addOperationViewModel.mode.value

        val cal = addOperationViewModel.date.value
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
            val operation = Operation(
                null,
                category,
                accountName,
                value.toString().toDouble(),
                datetime.toString(),
                comment
            )
            apiService = ApiClient.getClient(appPrefs.token.toString())

            apiService.addOperation(operation).enqueue(object : Callback<OperationResponse> {
                override fun onResponse(
                    call: Call<OperationResponse>,
                    response: Response<OperationResponse>
                ) {
                    val resp = response.body()
                    if (resp != null) {
                        if (type == AddOperationViewModel.Mode.EXPENSES)
                        {
                            if (resp.overLimitType == OverLimitType.CATEGORY) {
                                val alertDialogBuilder = AlertDialog.Builder(activity)
                                alertDialogBuilder.setMessage("Вы превысили лимит по категории!")

                                alertDialogBuilder.setNegativeButton("Добавить и закрыть") { dialog, _ ->
                                    dialog.dismiss()
                                    findNavController().navigate(R.id.homeFragment)
                                }
                                val alertDialog = alertDialogBuilder.create()
                                alertDialog.show()
                            } else if (resp.overLimitType == OverLimitType.ACCOUNT) {
                                val alertDialogBuilder = AlertDialog.Builder(requireContext())
                                alertDialogBuilder.setMessage("Вы превысили лимит по счету!")

                                alertDialogBuilder.setNegativeButton("Добавить и закрыть") { dialog, _ ->
                                    dialog.dismiss()
                                    findNavController().navigate(R.id.homeFragment)
                                }
                                val alertDialog = alertDialogBuilder.create()
                                alertDialog.show()
                            }
                        }

                        check = true
                    } else {

                        check = false
                    }

                }

                override fun onFailure(call: Call<OperationResponse>, t: Throwable) {
                    Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
                }

            })

        } else if (value.trim().isEmpty()) {
            check = false
            binding.editTextSum.setError("Введите сумму")
        }

        return check
    }

    private fun getAccounts() {
        apiService = ApiClient.getClient(appPrefs.token.toString())
        apiService.getAccounts().enqueue(object : Callback<List<Account>> {
            override fun onResponse(call: Call<List<Account>>, response: Response<List<Account>>) {
                val accounts = response.body()
                Log.d("accounts response", response.body().toString())
                if (accounts != null) {
                    addOperationViewModel.setAccounts(accounts)
                    Log.d("accounts", accounts.toString())
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
                    addOperationViewModel.setCategories(categories)
                    configureSpinnerCategories()
                }
                binding.loading.visibility = View.GONE
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
        val cur = Calendar.getInstance()
        binding.dateButton.setOnClickListener {
            context?.let { it1 ->
                val datePickerDialog = DatePickerDialog(
                    it1, this,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                )
                datePickerDialog.datePicker.maxDate = cur.time.time
                datePickerDialog.show()
            }
        }
    }

    override fun onDateSet(p0: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        cal.set(Calendar.YEAR, year)
        cal.set(Calendar.MONTH, monthOfYear)
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        TimePickerDialog(
            context, this,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE), true
        ).show()
    }

    override fun onTimeSet(p0: TimePicker?, hour: Int, minute: Int) {
        cal.set(Calendar.HOUR_OF_DAY, hour)
        cal.set(Calendar.MINUTE, minute)
        addOperationViewModel.setDate(cal)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            addOperationViewModel.setImageUri(data?.data)
            val imageUri = addOperationViewModel.imageUri.value
            if (imageUri != null) {
                binding.deletePhoto.isVisible = true
                binding.imageView.setImageURI(imageUri)
            }
        }
    }

}