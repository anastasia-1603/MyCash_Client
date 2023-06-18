package cs.vsu.ru.mycash.ui.main.diagrams

import android.app.DatePickerDialog
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import cs.vsu.ru.mycash.api.ApiClient
import cs.vsu.ru.mycash.api.ApiService
import cs.vsu.ru.mycash.data.Account
import cs.vsu.ru.mycash.data.diagrams.AnalyticsResponse
import cs.vsu.ru.mycash.databinding.FragmentDiagramsBinding
import cs.vsu.ru.mycash.utils.AppPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class DiagramsFragment : Fragment() {

    private var _binding: FragmentDiagramsBinding? = null
    private lateinit var apiService: ApiService
    private lateinit var appPrefs: AppPreferences
    private var cal = Calendar.getInstance()
    private val binding get() = _binding!!
    private val diagramsViewModel: DiagramsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        appPrefs = activity?.let { AppPreferences(it) }!!
        _binding = FragmentDiagramsBinding.inflate(inflater, container, false)
        binding.loading.visibility = View.VISIBLE

        getAccounts()

        if (diagramsViewModel.accountList.value != null && diagramsViewModel.accountList.value!!.size > 1) {
            binding.leftAccount.isVisible = true
            binding.rightAccount.isVisible = true
        } else {
            binding.leftAccount.isVisible = false
            binding.rightAccount.isVisible = false
        }

        val root: View = binding.root

        diagramsViewModel.accountIndex.observe(viewLifecycleOwner) {
            val accounts = diagramsViewModel.accountList.value
            if (accounts != null && accounts.isNotEmpty()) {
                val accountName = accounts[it].name.toString()
                binding.accountName.text = accountName
                getAnalytics()
            }
        }

        binding.leftAccount.setOnClickListener {
            Log.e("ind in left", diagramsViewModel.accountIndex.value.toString())
            diagramsViewModel.decrementAccountIndex()
            Log.e("ind in left after", diagramsViewModel.accountIndex.value.toString())
        }

        binding.rightAccount.setOnClickListener {
            Log.e("ind in right", diagramsViewModel.accountIndex.value.toString())
            diagramsViewModel.incrementAccountIndex()
            Log.e("ind in right after", diagramsViewModel.accountIndex.value.toString())
        }

        val dateBtn: Button = binding.date

        diagramsViewModel.date.observe(viewLifecycleOwner) {
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
            configureBtnRight()
        }

        binding.date.setOnClickListener {
            val maxC = Calendar.getInstance()
            val cur = Calendar.getInstance()
            maxC.set(Calendar.YEAR, cur.get(Calendar.YEAR))
            maxC.set(Calendar.MONTH, cur.get(Calendar.MONTH))
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
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1)
            diagramsViewModel.setDate(cal)
            getAnalytics()
        }

        binding.right.setOnClickListener {
            val date = diagramsViewModel.date.value
            if (date != null) {
                cal.set(Calendar.MONTH, date.get(Calendar.MONTH) + 1)
            }
            diagramsViewModel.setDate(cal)
            getAnalytics()
        }

        val viewPager = binding.viewPager
        viewPager.adapter = DiagramsPagerAdapter(this)
        val tabLayoutMediator =
            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = "Общий"
                    1 -> tab.text = "Доходы"
                    2 -> tab.text = "Расходы"
                }
            }
        tabLayoutMediator.attach()

        return root
    }


    private fun configureBtnRight() {
        val date = diagramsViewModel.date.value
        if (date != null) {
            if (date.get(Calendar.MONTH) == Calendar.getInstance().get(Calendar.MONTH)
                && date.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)
            ) {
                binding.right.isEnabled = false
            }
            else {
                binding.right.isEnabled = true
            }
        }
    }

    private fun getAnalytics() {
        setAllClickable(false)
        binding.loading.visibility = View.VISIBLE
        apiService = ApiClient.getClient(appPrefs.token.toString())
        val i = diagramsViewModel.accountIndex.value
        val account = i?.let { diagramsViewModel.accountList.value?.get(it) }
        val date = diagramsViewModel.date.value
        if (date != null && account != null)
        {
            val year = date.get(Calendar.YEAR).toString()
            val month = date.get(Calendar.MONTH) + 1
            Log.e("acc name diag", account.name.toString())
            apiService.getAnalytics(account.name, year, month.toString())
                .enqueue(object  : Callback<AnalyticsResponse> {
                    override fun onResponse(
                        call: Call<AnalyticsResponse>,
                        response: Response<AnalyticsResponse>
                    ) {
                        val resp = response.body()
                        if (resp != null)
                        {
                            diagramsViewModel.setData(resp)
                            binding.loading.visibility = View.GONE
                            setAllClickable(true)
                        }
                    }

                    override fun onFailure(call: Call<AnalyticsResponse>, t: Throwable) {
                        Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                        binding.loading.visibility = View.GONE
                    }

                })
        }

    }

    private fun getAccounts() {
        setAllClickable(false)
        apiService = ApiClient.getClient(appPrefs.token.toString())
        apiService.getAccounts().enqueue(object : retrofit2.Callback<List<Account>> {
            override fun onResponse(call: Call<List<Account>>, response: Response<List<Account>>) {
                val accounts = response.body()
                if (accounts != null) {
                    diagramsViewModel.setAccountList(accounts)
                    if (accounts.size > 1) {
                        binding.leftAccount.isVisible = true
                        binding.rightAccount.isVisible = true
                    } else {
                        binding.leftAccount.isVisible = false
                        binding.rightAccount.isVisible = false
                    }
                    binding.accountName.text = diagramsViewModel.accountName.value
                    getAnalytics()
                }
            }

            override fun onFailure(call: Call<List<Account>>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class DiagramsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int {
            return 3
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> DiagramsAllFragment()
                1 -> DiagramsIncomeFragment()
                2 -> DiagramsExpensesFragment()
                else -> throw IllegalArgumentException("Invalid position")
            }
        }
    }

    private fun datePickerDialog(): DatePickerDialog? {
        val c = diagramsViewModel.date.value ?: Calendar.getInstance()

        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = context?.let {
            DatePickerDialog(
                it, android.R.style.Theme_Holo_Light_Dialog, { _, year, monthOfYear, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    diagramsViewModel.setDate(cal)
                }, year, month, day
            )
        }
        if (datePickerDialog != null) {
            val maxC = Calendar.getInstance()
            val cur = Calendar.getInstance()
            maxC.set(Calendar.YEAR, cur.get(Calendar.YEAR))
            maxC.set(Calendar.MONTH, cur.get(Calendar.MONTH))
            maxC.set(Calendar.DAY_OF_MONTH, 1)
            datePickerDialog.datePicker.maxDate = maxC.time.time
        }
        return datePickerDialog
    }


    private fun setAllClickable(isClickable: Boolean) {
        binding.right.isClickable = isClickable
        binding.left.isClickable = isClickable
        binding.rightAccount.isClickable = isClickable
        binding.leftAccount.isClickable = isClickable
        binding.date.isClickable = isClickable
    }
}