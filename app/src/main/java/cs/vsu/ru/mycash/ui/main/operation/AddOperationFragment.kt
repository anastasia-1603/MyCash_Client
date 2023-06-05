package cs.vsu.ru.mycash.ui.main.operation

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import cs.vsu.ru.mycash.R
import cs.vsu.ru.mycash.api.ApiClient
import cs.vsu.ru.mycash.api.ApiService
import cs.vsu.ru.mycash.data.*
import cs.vsu.ru.mycash.databinding.FragmentAddOperationBinding
import cs.vsu.ru.mycash.ui.main.home.OperationViewModel
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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        addOperationViewModel = ViewModelProvider(this)[AddOperationViewModel::class.java]
        appPrefs = activity?.let { AppPreferences(it) }!!
        _binding = FragmentAddOperationBinding.inflate(inflater, container, false)

        getAccounts()
        Log.e("acc onc", addOperationViewModel.accounts.value.toString())
        getCategories()
        Log.e("cat onc", addOperationViewModel.categories.value.toString())

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
            if (postOperation()) {
                findNavController().navigateUp()
            }
        }

        binding.cancelButton.setOnClickListener {
            findNavController().navigateUp()
        }


//        val imageView: ImageView = binding.imageView
//
//        val button: Button = findViewById(R.id.button)
//        button.setOnClickListener {
//            val uri: Uri = Uri.parse("android.resource://$packageName/${R.drawable.cat}")
//            // remove previous image uri cache
//            imageView.setImageURI(null)
//            // set image view image from uri
//            imageView.setImageURI(uri)
//            // show the uri in text view
//            textView.text = uri.toString()
//        }
//        binding.photoBtn.setOnClickListener {
//            val selectImageIntent = registerForActivityResult(ActivityResultContracts.GetContent())
//            { uri ->
//                binding.imageView.setImageURI(uri)
//            }
//            selectImageIntent.launch("image/*")
//        }

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
        val account = addOperationViewModel.accounts.value?.filter { it.name == accountName }
        val value = binding.editTextSum.text

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
                    Log.d("oper response", response.body().toString())
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
            binding.editTextSum.setError("Введите сумму")
        }

        return check
    }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        addOperationViewModel = ViewModelProvider(this)[AddOperationViewModel::class.java]
//        appPrefs = activity?.let { AppPreferences(it) }!!
//
//        Log.e("acc att", addOperationViewModel.accounts.value.toString())
//        getCategories()
//        getAccounts()
//        Log.e("cat att", addOperationViewModel.categories.value.toString())
//    }

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
        addOperationViewModel.setDate(cal)
    }

}