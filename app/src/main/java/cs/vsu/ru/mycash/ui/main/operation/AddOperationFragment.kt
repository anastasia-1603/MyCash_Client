package cs.vsu.ru.mycash.ui.main.operation

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import cs.vsu.ru.mycash.api.ApiClient
import cs.vsu.ru.mycash.api.ApiService
import cs.vsu.ru.mycash.data.Account
import cs.vsu.ru.mycash.data.Category
import cs.vsu.ru.mycash.data.Operation
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        addOperationViewModel = ViewModelProvider(this)[AddOperationViewModel::class.java]
        _binding = FragmentAddOperationBinding.inflate(inflater, container, false)
        appPrefs = activity?.let { AppPreferences(it) }!!

        binding.incomeBtn.isEnabled = false
        binding.spendingButton.isEnabled = true

        binding.spendingButton.setOnClickListener {
            addOperationViewModel.setMode(AddOperationViewModel.Mode.EXPENSES)
        }
        binding.incomeBtn.setOnClickListener {
            addOperationViewModel.setMode(AddOperationViewModel.Mode.INCOME)
        }
        binding.dateButton.setOnClickListener {

        }

        pickDate()

        addOperationViewModel.date.observe(viewLifecycleOwner) {
            val dateFormat = "dd.MM.yyyy hh:mm"
            val current = Calendar.getInstance()
            val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())

            if (sdf.format(it.time).equals(sdf.format(current.time))) {

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
        }

        getAccounts()
        getCategories()
        configureSpinnerAccounts()
        configureSpinnerCategories()


        return binding.root
    }

    private fun configureSpinnerCategories() {
        if (addOperationViewModel.categories.value != null) {

            val categoriesNames = addOperationViewModel.categories.value!!
                .stream()
                .map { it.name }.toList() as ArrayList<String>
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
    private fun postOperation() {
        val categoryName = addOperationViewModel.categoryName.value
        val category = addOperationViewModel.categories.value?.find { it.name == categoryName }
        val accountName = addOperationViewModel.accountName.value
        val account = addOperationViewModel.accounts.value?.filter { it.name == accountName }
        val value = binding.editTextSum.text
        val cal = addOperationViewModel.date.value
        val comment = binding.comment.text.toString()
        if (category != null
            && accountName != null
            && value != null
            && cal != null) {
            val datetime: LocalDateTime = LocalDateTime.ofInstant(
                cal.toInstant(), cal.timeZone.toZoneId()
            )
            val operation: Operation =
                Operation(
                    null,
                    category,
                    accountName,
                    value.toString().toDouble(),
                    datetime,
                    comment
                )
            apiService = ApiClient.getClient(appPrefs.token.toString())
            apiService.addOperation(operation)
        }

    }

    private fun getAccounts() {
        apiService = ApiClient.getClient(appPrefs.token.toString())
        apiService.getAccounts().enqueue(object : Callback<List<Account>> {
            override fun onResponse(call: Call<List<Account>>, response: Response<List<Account>>) {
                val accounts = response.body()
                if (accounts != null) {
                    addOperationViewModel.setAccounts(accounts)
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
                if (categories != null) {
                    addOperationViewModel.setCategories(categories)
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