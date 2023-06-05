package cs.vsu.ru.mycash.ui.main.home

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import cs.vsu.ru.mycash.R
import cs.vsu.ru.mycash.api.ApiClient
import cs.vsu.ru.mycash.api.ApiService
import cs.vsu.ru.mycash.data.AccountDto
import cs.vsu.ru.mycash.data.Operation
import cs.vsu.ru.mycash.data.PredictionResponse
import cs.vsu.ru.mycash.databinding.FragmentPredictBinding
import cs.vsu.ru.mycash.utils.AppPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class PredictFragment : Fragment() {

    private var _binding: FragmentPredictBinding? = null
    private var cal = Calendar.getInstance()
    private val dateFormat = "d MMMM"
    private lateinit var predictViewModel: PredictViewModel
    private val homeViewModel :HomeViewModel by activityViewModels()
    private val binding get() = _binding!!

    private lateinit var apiService: ApiService
    private lateinit var appPrefs: AppPreferences

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        appPrefs = activity?.let { AppPreferences(it) }!!
        _binding = FragmentPredictBinding.inflate(inflater, container, false)
        predictViewModel = ViewModelProvider(this)[PredictViewModel::class.java]
        homeViewModel.accountList.value?.let { predictViewModel.setAccountsList(it) }
        homeViewModel.date.value?.let { predictViewModel.setDate(it) }
        homeViewModel.accountName.value?.let { predictViewModel.setAccountName(it) }
        val root: View = binding.root

        val accountName: TextView = binding.accountName
        predictViewModel.accountName.observe(viewLifecycleOwner) {
            accountName.text = it
        }

        val balance: TextView = binding.balancePredict
        predictViewModel.balance.observe(viewLifecycleOwner) {
            balance.text = it
        }
        val dateBtn: Button = binding.date

        predictViewModel.accountIndex.observe(viewLifecycleOwner) {
            val accounts = predictViewModel.accountList.value
            val index = it
            if (index != null) {
                val account = accounts?.get(index)
                if (account != null) {
                    predictViewModel.setAccountName(account.name.toString())
                    getPredict()
                }
            }
        }

        predictViewModel.date.observe(viewLifecycleOwner) {
            val cur = Calendar.getInstance()
            if (it.get(Calendar.YEAR) != cur.get(Calendar.YEAR) ||
                    it.get(Calendar.MONTH) > cur.get(Calendar.MONTH) + 1)
            {
                findNavController().navigate(R.id.homeFragment)
            }
            else {
                val monthNames = arrayOf(
                    "Январь",
                    "Февраль",
                    "Март",
                    "Апрель",
                    "Май",
                    "Июнь",
                    "Июль",
                    "Август",
                    "Сентябрь",
                    "Октябрь",
                    "Ноябрь",
                    "Декабрь"
                )

                dateBtn.text = monthNames[cal.get(Calendar.MONTH)]
            }

        }



        binding.date.setOnClickListener {
            val dialog = datePickerDialog()
            dialog?.show()
            val day = dialog?.findViewById<View>(
                Resources.getSystem().getIdentifier("android:id/day", null, null)
            )
            if (day != null) {
                day.visibility = View.GONE
            }

        }

        binding.left.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }

        binding.right.isEnabled = false

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.addOperationFragment)
        }

        if (predictViewModel.accountList.value != null
            && predictViewModel.accountList.value!!.size > 1
        ) {
            binding.leftAccount.isVisible = true
            binding.rightAccount.isVisible = true
        } else {
            binding.leftAccount.isVisible = false
            binding.rightAccount.isVisible = false
        }

        binding.leftAccount.setOnClickListener {
            predictViewModel.decrementAccountIndex()
        }

        binding.rightAccount.setOnClickListener {
            predictViewModel.incrementAccountIndex()
        }
        return root
    }

    override fun onResume() {
        super.onResume()
        getPredict()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    fun datePickerDialog(): DatePickerDialog? {
        val c = predictViewModel.date.value ?: Calendar.getInstance()

        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog =
            context?.let {
                DatePickerDialog(
                    it,
                    android.R.style.Theme_Holo_Light_Dialog,
                    { _, year, monthOfYear, dayOfMonth ->
                        cal.set(Calendar.YEAR, year)
                        cal.set(Calendar.MONTH, monthOfYear)
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        predictViewModel.setDate(cal)
                    },
                    year,
                    month,
                    day
                )
            }
        if (datePickerDialog != null) {
            val maxC = Calendar.getInstance()
            val cur = Calendar.getInstance()
            maxC.set(Calendar.YEAR, cur.get(Calendar.YEAR))
            maxC.set(Calendar.MONTH, cur.get(Calendar.MONTH) + 1)
            datePickerDialog.datePicker.maxDate =  maxC.time.time
        }
        return datePickerDialog
    }

    private fun getPredict()
    {
        apiService = ApiClient.getClient(appPrefs.token.toString())
        val date = predictViewModel.date.value
        if (date != null) {
            val call: Call<PredictionResponse> =
                apiService.getPredict(
                    binding.accountName.text.toString(),
                    date.get(Calendar.YEAR),
                    date.get(Calendar.MONTH))
            call.enqueue(object : Callback<PredictionResponse> {
                override fun onResponse(
                    call: Call<PredictionResponse>,
                    response: Response<PredictionResponse>
                ) {
                    val resp = response.body()
                    if (resp != null)
                    {
                        predictViewModel.setCategory(resp.topCategory)
                        predictViewModel.setPredictExp(resp.expensePrediction)
                        predictViewModel.setPredictInc(resp.incomePrediction)
                        predictViewModel.setPredictSum(resp.topCategoryPrediction)
                        val balance = homeViewModel.balance.value?.toDouble()

                        if (balance != null) {
                            predictViewModel.setBalance(
                                balance + resp.incomePrediction - resp.expensePrediction)
                        }
                    }
                }
                override fun onFailure(call: Call<PredictionResponse>, t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }

            })
        }


    }

}


