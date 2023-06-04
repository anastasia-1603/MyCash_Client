package cs.vsu.ru.mycash.ui.main.home

import android.app.DatePickerDialog
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
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import cs.vsu.ru.mycash.R
import cs.vsu.ru.mycash.api.ApiClient
import cs.vsu.ru.mycash.api.ApiService
import cs.vsu.ru.mycash.data.*
import cs.vsu.ru.mycash.databinding.FragmentHomeBinding
import cs.vsu.ru.mycash.utils.AppPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private var cal = Calendar.getInstance()
    private val dateFormat = "d MMMM"
    private val operationViewModel: OperationViewModel by activityViewModels()
    private lateinit var homeViewModel: HomeViewModel
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

        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val accountName: TextView = binding.accountName
        homeViewModel.accountName.observe(viewLifecycleOwner) {
            accountName.text = it
        }

        val balance: TextView = binding.balance
        homeViewModel.balance.observe(viewLifecycleOwner) {
            balance.text = it
        }

        homeViewModel.accountIndex.observe(viewLifecycleOwner) {
            val accounts = operationViewModel.map.value?.let { it1 -> getAccounts(it1) }
            val index = it
            if (index != null) {
                val account = accounts?.get(index)
                if (account != null) {
                    updateAccount(account)
                }
            }
        }

        val dateBtn : Button = binding.date
        val dayBtn: Button = binding.day
        val monthBtn: Button = binding.month

        dayBtn.setOnClickListener {
            homeViewModel.setMode(HomeViewModel.Mode.DAY)
            dayBtn.isEnabled = false
            monthBtn.isEnabled = true

        }

        monthBtn.setOnClickListener {
            homeViewModel.setMode(HomeViewModel.Mode.MONTH)
            dayBtn.isEnabled = true
            monthBtn.isEnabled = false
        }

        homeViewModel.mode.observe(viewLifecycleOwner) {
            if (homeViewModel.mode.value == HomeViewModel.Mode.DAY) {
                if (!dayBtn.isEnabled) {
                    dayBtn.isEnabled = true
                    monthBtn.isEnabled = false
                }
                val current = Calendar.getInstance()
                val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
                val sdf1 = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

                if (sdf1.format(cal.time).equals(sdf1.format(current.time))) {
                    dateBtn.text = "Сегодня"
                } else {
                    dateBtn.text = sdf.format(cal.time)
                }

            } else {
                if (!monthBtn.isEnabled) {
                    monthBtn.isEnabled = true
                    dayBtn.isEnabled = false
                }
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

        homeViewModel.date.observe(viewLifecycleOwner) {
            if (homeViewModel.mode.value == HomeViewModel.Mode.DAY) {
                val current = Calendar.getInstance()
                val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
                val sdf1 = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

                if (sdf1.format(it.time).equals(sdf1.format(current.time))) {
                    dateBtn.text = "Сегодня"
                } else {
                    dateBtn.text = sdf.format(it.time)
                }

            } else {
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


        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                homeViewModel.setDate(cal)
            }

        binding.date.setOnClickListener {
            if (homeViewModel.mode.value == HomeViewModel.Mode.DAY) {
                context?.let { it1 ->
                    DatePickerDialog(
                        it1,
                        dateSetListener,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }
            } else {
                Log.e("month", "month")
            }

        }

        binding.left.setOnClickListener {
            if (homeViewModel.mode.value == HomeViewModel.Mode.DAY) {
                cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - 1)
            } else {
                cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1)
            }
            loadOperations()
            homeViewModel.setDate(cal)
        }

        binding.right.setOnClickListener {
            if (homeViewModel.mode.value == HomeViewModel.Mode.DAY) {
                cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1)

            } else {
                cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1)
            }
            loadOperations()
            homeViewModel.setDate(cal)
        }

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.addOperationFragment)
        }

        val viewPager = binding.viewPager
        viewPager.adapter = HomePagerAdapter(this)
        val tabLayoutMediator =
            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = "Все"
                    1 -> tab.text = "Расходы"
                    2 -> tab.text = "Доходы"
                }
            }
        tabLayoutMediator.attach()


        return root
    }

    override fun onResume() {
        super.onResume()
        loadOperations()
    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        loadOperations()
//    }



    class HomePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int {
            return 3
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> TabAllFragment()
                1 -> TabExpensesFragment()
                2 -> TabIncomeFragment()
                else -> throw IllegalArgumentException("Invalid position")
            }
        }
    }


    private fun loadOperations() {
        apiService = ApiClient.getClient(appPrefs.token.toString())
        val call: Call<Map<String, List<Operation>>>
        if (homeViewModel.mode.value == HomeViewModel.Mode.DAY)
        {
            call = apiService.getDataByDay(
                cal.get(Calendar.YEAR), //todo поменять на date
                cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.DAY_OF_MONTH)
            )
        }
        else
        {
            call = apiService.getDataByMonth(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH) + 1)
        }
        call.enqueue(object : Callback<Map<String, List<Operation>>> {
            override fun onResponse(
                call: Call<Map<String, List<Operation>>>,
                response: Response<Map<String, List<Operation>>>
            ) {
                val map = response.body()
                Log.e("map", map.toString())
                if (map != null)
                {
                    updateMap(map)
                }
            }
            override fun onFailure(call: Call<Map<String, List<Operation>>>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }

        })
    }


    private fun updateOperations(operationList: List<Operation>)
    {
        operationViewModel.setOperationList(operationList)
        operationViewModel.operationList.value?.let { operations ->
            operationViewModel.setExpenseList(operations.filter {
                it.category.type == CategoryType.EXPENSE
            })
        }
        operationViewModel.operationList.value?.let { operations ->
            operationViewModel.setIncomeList(
                operations.filter {
                    it.category.type == CategoryType.INCOME
                })
        }
    }

    private fun updateAccount(account: AccountDto, operationList: List<Operation>)
    {
        homeViewModel.setAccountName(account.name)
        homeViewModel.setBalance(account.balance.toString())
        updateOperations(operationList)
    }

    private fun updateAccount(account: AccountDto)
    {
        homeViewModel.setAccountName(account.name)
        homeViewModel.setBalance(account.balance.toString())
        val operationList = operationViewModel.map.value?.get(account.name)
        if (operationList != null) {
            updateOperations(operationList)
        }
    }
    private fun updateMap(map: Map<String, List<Operation>>)
    {
        operationViewModel.setMap(map)
        val accounts : List<AccountDto> = getAccounts(map)
        val index = homeViewModel.accountIndex.value
        if (index != null) {
            val account = accounts[index]
            map[account.name]?.let { updateAccount(account, it) }
        }
    }


    private fun updateDate(newDate : Calendar) {

        homeViewModel.setDate(newDate)
        if (homeViewModel.mode.value == HomeViewModel.Mode.DAY) {
            val current = Calendar.getInstance()
            val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
            val sdf1 = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

            if (sdf1.format(newDate.time).equals(sdf1.format(current.time))) {
                binding.date.text = "Сегодня"
            } else {
                binding.date.text = sdf.format(newDate.time)
            }

        } else {
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
            binding.date.text = monthNames[newDate.get(Calendar.MONTH)]
        }
    }

    private fun getAccounts(map: Map<String, List<Operation>>) : List<AccountDto>
    {
        val accounts : ArrayList<AccountDto> = ArrayList()
        map.keys.forEach {
            val tmp = it.split(":")
            val accountDto : AccountDto = AccountDto(tmp[0], tmp[1].toDouble())
            accounts.add(accountDto)
        }
        return accounts
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}

