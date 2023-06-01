package cs.vsu.ru.mycash.ui.main.home

import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import cs.vsu.ru.mycash.R
import cs.vsu.ru.mycash.api.ApiAuthClient
import cs.vsu.ru.mycash.api.ApiService
import cs.vsu.ru.mycash.data.*
import cs.vsu.ru.mycash.databinding.FragmentHomeBinding
import cs.vsu.ru.mycash.utils.AppPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private var cal = Calendar.getInstance()
    private val dateFormat = "d MMMM"
    private lateinit var appPrefs: AppPreferences
    private val operationViewModel : OperationViewModel by activityViewModels()
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        appPrefs = activity?.let { AppPreferences(it) }!!
        val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

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
        val dateBtn: Button = binding.date
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
            if (homeViewModel.mode.value == HomeViewModel.Mode.DAY)
            {
                if (!dayBtn.isEnabled)
                {
                    dayBtn.isEnabled = true
                    monthBtn.isEnabled = false
                }
                val current = Calendar.getInstance()
                val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
                val sdf1 = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

                if (sdf1.format(cal.time).equals(sdf1.format(current.time)))
                {
                    dateBtn.text = "Сегодня"
                }
                else
                {
                    dateBtn.text = sdf.format(cal.time)
                }

            }
            else
            {
                if (!monthBtn.isEnabled)
                {
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
            if (homeViewModel.mode.value == HomeViewModel.Mode.DAY)
            {
                val current = Calendar.getInstance()
                val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
                val sdf1 = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())

                if (sdf1.format(it.time).equals(sdf1.format(current.time)))
                {
                    dateBtn.text = "Сегодня"
                }
                else
                {
                    dateBtn.text = sdf.format(it.time)
                }

            }
            else
            {
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
            if (homeViewModel.mode.value == HomeViewModel.Mode.DAY)
            {
                context?.let { it1 ->
                    DatePickerDialog(
                        it1,
                        dateSetListener,
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)
                    ).show()
                }
            }
            else
            {
                Log.e("month", "month")

            }

        }

        binding.left.setOnClickListener {
            if (homeViewModel.mode.value == HomeViewModel.Mode.DAY)
            {
                cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - 1)
            }
            else
            {
                cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1)
            }
            homeViewModel.setDate(cal)

        }

        binding.right.setOnClickListener {
            if (homeViewModel.mode.value == HomeViewModel.Mode.DAY)
            {
                cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1)

            }
            else
            {
                cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1)
            }
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

//        val preferences = activity?.getSharedPreferences("MY_APP", Context.MODE_PRIVATE)

        val token = appPrefs.token
        if (token != null) {
            Log.e("token home", token)
        }
        Log.e("token home prefs", appPrefs.token.toString())
        //val apiService = token?.let { ApiAuthClient.getClient(it).create(ApiService::class.java) }
        val apiService = ApiAuthClient.getClient(appPrefs.token.toString()).create(ApiService::class.java)
        Log.e("token home prefs", appPrefs.token.toString())
        homeViewModel.setAccountName("основа")

        apiService?.getAccountInfo(
            "основа",
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH) + 1,
            cal.get(Calendar.DAY_OF_MONTH)
        )?.enqueue(object : Callback<List<Operation>> {
            override fun onResponse(
                call: Call<List<Operation>>,
                response: Response<List<Operation>>
            ) {
                response.body()?.let { operationViewModel.setOperationList(it) }
                operationViewModel.operationList.value?.let { operations ->
                    operationViewModel.setExpenseList(
                        operations.filter { it.category.type == CategoryType.EXPENSE })
                }
                operationViewModel.operationList.value?.let { operations ->
                    operationViewModel.setIncomeList(
                        operations.filter { it.category.type == CategoryType.INCOME })
                }

            }

            override fun onFailure(call: Call<List<Operation>>, t: Throwable) {
                t.message?.let { Log.e("t", it) }
            }
        })

        return root
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

