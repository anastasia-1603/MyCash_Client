package cs.vsu.ru.mycash.ui.main.home

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
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
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import cs.vsu.ru.mycash.R
import cs.vsu.ru.mycash.api.ApiClient
import cs.vsu.ru.mycash.api.ApiService
import cs.vsu.ru.mycash.data.*
import cs.vsu.ru.mycash.databinding.FragmentHomeBinding
import cs.vsu.ru.mycash.ui.main.operation.OperationViewModel
import cs.vsu.ru.mycash.utils.AppPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.nio.file.Files.find
import java.text.SimpleDateFormat
import java.util.*
import kotlin.streams.toList

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private var cal = Calendar.getInstance()
    private val dateFormat = "d MMMM"
    private val operationViewModel: OperationViewModel by activityViewModels()
    private val accountSendViewModel: AccountSendViewModel by activityViewModels()
    private lateinit var homeViewModel: HomeViewModel
    private val binding get() = _binding!!

    private lateinit var apiService: ApiService
    private lateinit var appPrefs: AppPreferences

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        appPrefs = activity?.let { AppPreferences(it) }!!

        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.loading.visibility = View.VISIBLE
        val accountName: TextView = binding.accountName
        homeViewModel.accountName.observe(viewLifecycleOwner) {
            accountName.text = it
        }

        val balance: TextView = binding.balance
        homeViewModel.balance.observe(viewLifecycleOwner) {
            balance.text = "$it ₽"
        }

        homeViewModel.accountIndex.observe(viewLifecycleOwner) {
            val accounts = homeViewModel.accountList.value
//            val accounts = operationViewModel.data.value?.let { it1 -> getAccounts(it1) }
            val index = it
            if (index != null) {
                val account = accounts?.get(index)
                if (account != null) {
                    updateAccount(account)
                }
            }
        }

        val dateBtn: Button = binding.date
        val dayBtn: Button = binding.day
        val monthBtn: Button = binding.month

        if (dayBtn.isEnabled == monthBtn.isEnabled) {
            if (homeViewModel.mode.value == HomeViewModel.Mode.DAY) {
                dayBtn.isEnabled = false
                monthBtn.isEnabled = true
            } else {
                dayBtn.isEnabled = true
                monthBtn.isEnabled = false
            }
        }


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
                val current = Calendar.getInstance()
                val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
                val sdf1 = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

                if (sdf1.format(cal.time).equals(sdf1.format(current.time))) {
                    dateBtn.text = "Сегодня"
                } else {
                    dateBtn.text = sdf.format(cal.time)
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
            configureBtnRight()
        }

        homeViewModel.date.observe(viewLifecycleOwner) {
            val current = Calendar.getInstance()
            if (homeViewModel.mode.value == HomeViewModel.Mode.DAY) {
                val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
                val sdf1 = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

                if (sdf1.format(it.time).equals(sdf1.format(current.time))) {
                    dateBtn.text = "Сегодня"
                } else {
                    dateBtn.text = sdf.format(it.time)
                }

            } else {
                if (it.get(Calendar.MONTH) == current.get(Calendar.MONTH) + 1 && it.get(Calendar.YEAR) == it.get(
                        Calendar.YEAR
                    )
                ) {
                    findNavController().navigate(R.id.predictFragment)
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
            configureBtnRight()
        }

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                homeViewModel.setDate(cal)
            }

        binding.date.setOnClickListener {
            val maxC = Calendar.getInstance()
            val cur = Calendar.getInstance()
            maxC.set(Calendar.YEAR, cur.get(Calendar.YEAR))
            maxC.set(Calendar.MONTH, cur.get(Calendar.MONTH))
            if (homeViewModel.mode.value == HomeViewModel.Mode.DAY) {
                context?.let { it1 ->
                    val datePickerDialog = DatePickerDialog(
                        it1,
                        dateSetListener,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)
                    )
                    datePickerDialog.datePicker.maxDate = maxC.time.time
                    datePickerDialog.show()
                }
            } else {
                val dialog = datePickerDialog()
                dialog?.show()
                val day = dialog?.findViewById<View>(
                    Resources.getSystem().getIdentifier("android:id/day", null, null)
                )
                if (day != null) {
                    day.visibility = View.GONE
                }
            }
        }

        binding.left.setOnClickListener {
            if (homeViewModel.mode.value == HomeViewModel.Mode.DAY) {
                cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - 1)
            } else {
                cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1)
            }
            homeViewModel.setDate(cal)
            loadOperations()
        }

        binding.right.setOnClickListener {
            val cur = Calendar.getInstance()
            if (homeViewModel.mode.value == HomeViewModel.Mode.DAY) {
                val sdf1 = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

                if (sdf1.format(cal.time).equals(sdf1.format(cur.time))) {

                } else {
                    cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1)
                }

            } else {
                val date = homeViewModel.date.value
                if (date != null) {
                    if (date.get(Calendar.MONTH) == cur.get(Calendar.MONTH) + 1 && date.get(Calendar.YEAR) == cur.get(
                            Calendar.YEAR
                        )
                    ) {
                        val ind = homeViewModel.accountIndex.value
                        val accounts = homeViewModel.accountList.value
                        if (ind != null) {
                            accountSendViewModel.setAccountIndex(ind)
                        }
                        if (accounts != null) {
                            accountSendViewModel.setAccountsList(accounts)
                        }
                        accountSendViewModel.setBalance(
                            homeViewModel.balance.value.toString().toDouble()
                        )
                        findNavController().navigate(R.id.predictFragment)
                    } else {
                        cal.set(Calendar.MONTH, date.get(Calendar.MONTH) + 1)
                    }
                }
            }
            homeViewModel.setDate(cal)
            loadOperations()
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

        if (homeViewModel.accountList.value != null && homeViewModel.accountList.value!!.size > 1) {
            binding.leftAccount.isVisible = true
            binding.rightAccount.isVisible = true
        } else {
            binding.leftAccount.isVisible = false
            binding.rightAccount.isVisible = false
        }

        binding.leftAccount.setOnClickListener {
            homeViewModel.decrementAccountIndex()
        }

        binding.rightAccount.setOnClickListener {
            homeViewModel.incrementAccountIndex()
        }
        return root
    }


    private fun configureBtnRight() {
        if (homeViewModel.mode.value == HomeViewModel.Mode.DAY) {
            val date = homeViewModel.date.value
            val sdf1 = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
            if (date != null) {
                binding.right.isEnabled =
                    !sdf1.format(date.time).equals(sdf1.format(Calendar.getInstance().time))
            }
        } else {
            binding.right.isEnabled = true

        }
    }

    override fun onResume() {
        super.onResume()
        loadOperations()
        homeViewModel.accountList.value?.let { accountSendViewModel.setAccountsList(it) }

    }

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
        binding.loading.visibility = View.VISIBLE
        apiService = ApiClient.getClient(appPrefs.token.toString())
//        val call: Call<Map<String, List<Operation>>>
        val call: Call<List<MainScreenAccountResponse>>
        val calVm = homeViewModel.date.value

        if (calVm != null) {
            if (!calVm.time.after(Calendar.getInstance().time)) {
                call = if (homeViewModel.mode.value == HomeViewModel.Mode.DAY) {
                    apiService.getDataByDay(
                        calVm.get(Calendar.YEAR),
                        calVm.get(Calendar.MONTH) + 1,
                        calVm.get(Calendar.DAY_OF_MONTH)
                    )
                } else {
                    apiService.getDataByMonth(
                        calVm.get(Calendar.YEAR), calVm.get(Calendar.MONTH) + 1
                    )
                }
                call.enqueue(object : Callback<List<MainScreenAccountResponse>> {
                    override fun onResponse(
                        call: Call<List<MainScreenAccountResponse>>,
                        response: Response<List<MainScreenAccountResponse>>
                    ) {
//                        val map = response.body()
//                        if (map != null) {
//                            updateMap(map)
//                        }
                        val resp = response.body()
                        if (resp != null)
                        {
                            update(resp)
                        }
                        homeViewModel.accountList.value?.let {
                            accountSendViewModel.setAccountsList(it)
                        }
                        binding.loading.visibility = View.GONE
                    }

                    override fun onFailure(call: Call<List<MainScreenAccountResponse>>, t: Throwable) {
                        Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                    }

                })
            }

        }
    }

    private fun update(data : List<MainScreenAccountResponse>) {

        operationViewModel.setData(data)
        val accounts: List<AccountDto> = data.stream().map {
            AccountDto(it.accountName, it.balance)
        }.toList()
        homeViewModel.setAccountsList(accounts)
        val index = homeViewModel.accountIndex.value
        if (index != null) {
            val account = accounts[index]
            homeViewModel.setAccountName(account.name)
            homeViewModel.setBalance(account.balance.toString())
            val operationsList : List<Operation>? = data.find {
                it.accountName == account.name
            }?.operations

            if (operationsList != null) {
                updateOperations(operationsList)
            }
        }

    }

    private fun updateOperations(operationList: List<Operation>) {
        operationViewModel.setOperationList(operationList)
        operationViewModel.operationList.value?.let { operations ->
            operationViewModel.setExpenseList(operations.filter {
                it.category.type == CategoryType.EXPENSE
            })
        }
        operationViewModel.operationList.value?.let { operations ->
            operationViewModel.setIncomeList(operations.filter {
                it.category.type == CategoryType.INCOME
            })
        }
    }

//    private fun updateAccount(account: AccountDto, operationList: List<Operation>) {
//        homeViewModel.setAccountName(account.name)
//        homeViewModel.setBalance(account.balance.toString())
//        updateOperations(operationList)
//    }

    private fun updateAccount(account: AccountDto) {
        homeViewModel.setAccountName(account.name)
        homeViewModel.setBalance(account.balance.toString())
        val data = operationViewModel.data.value
        if (data != null) {
            val operationsList : List<Operation>? = data.find {
                it.accountName == account.name
            }?.operations

            if (operationsList != null) {
                updateOperations(operationsList)
            }

        }



    }

//    private fun updateMap(map: Map<String, List<Operation>>) {
//        operationViewModel.setMap(map)
//
//        val accounts: List<AccountDto> = getAccounts(map)
//        homeViewModel.setAccountsList(accounts)
//
//        val index = homeViewModel.accountIndex.value
//        if (index != null) {
//            val account = accounts[index]
//
//            map["${account.name}:${account.balance}"]?.let {
//                updateAccount(account, it)
//            }
//        }
//    }

//    private fun getAccounts(map: Map<String, List<Operation>>): List<AccountDto> {
//        val accounts: ArrayList<AccountDto> = ArrayList()
//        map.keys.forEach {
//            val tmp = it.split(":")
//            val accountDto = AccountDto(tmp[0], tmp[1].toDouble())
//            accounts.add(accountDto)
//        }
//        return accounts
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    fun datePickerDialog(): DatePickerDialog? {
        val c = homeViewModel.date.value ?: Calendar.getInstance()

        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = context?.let {
            DatePickerDialog(
                it, android.R.style.Theme_Holo_Light_Dialog, { _, year, monthOfYear, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, monthOfYear)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    homeViewModel.setDate(cal)
                }, year, month, day
            )
        }
        if (datePickerDialog != null) {
            val maxC = Calendar.getInstance()
            val cur = Calendar.getInstance()
            maxC.set(Calendar.YEAR, cur.get(Calendar.YEAR))
            maxC.set(Calendar.MONTH, cur.get(Calendar.MONTH) + 1)
            maxC.set(Calendar.DAY_OF_MONTH, 1)
            datePickerDialog.datePicker.maxDate = maxC.time.time
        }
        return datePickerDialog
    }
}

