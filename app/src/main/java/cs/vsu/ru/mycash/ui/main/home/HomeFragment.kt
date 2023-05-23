package cs.vsu.ru.mycash.ui.main.home

import android.app.DatePickerDialog
import android.content.Context
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import by.dzmitry_lakisau.month_year_picker_dialog.MonthYearPickerDialog
import com.google.android.material.tabs.TabLayoutMediator
import cs.vsu.ru.mycash.R
import cs.vsu.ru.mycash.adapter.OperationAdapter
import cs.vsu.ru.mycash.data.*
import cs.vsu.ru.mycash.databinding.FragmentHomeBinding
import cs.vsu.ru.mycash.service.OperationService
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var adapter: OperationAdapter
    private lateinit var operationService: OperationService
    private var cal = Calendar.getInstance()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        homeViewModel.setBalance("1000") //test
        homeViewModel.setAccountName("Новый счет") //test

        val accountName: TextView = binding.accountName
        homeViewModel.accountName.observe(viewLifecycleOwner) {
            accountName.text = it
        }

        val balance: TextView = binding.balance
        homeViewModel.balance.observe(viewLifecycleOwner) {
            balance.text = it
        }

        val date: Button = binding.date
        homeViewModel.date.observe(viewLifecycleOwner) {
            date.text = it
        }

//        this.context?.let {
//            MonthYearPickerDialog.Builder(
//                context = it,
//                themeResId = R.style.Theme_MyCash,
//                onDateSetListener = { year, month ->
//                    date.text = year.toString()
//                }
//            )
//                .setNegativeButton("Отменить")
//                .setPositiveButton("Ок")
//                .build()
//        }

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateDateInView()
            }

        binding.date.setOnClickListener {
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

        val viewPager = binding.viewPager
        viewPager.adapter = HomePagerAdapter(this)
        val tabLayoutMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager){ tab, position ->
            when(position) {
                0 -> tab.text = "Все"
                1 -> tab.text = "Расходы"
                2 -> tab.text = "Доходы"
            }
        }
        tabLayoutMediator.attach()

        val preferences = activity?.getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
        val token = preferences?.getString("TOKEN", "token")
//        binding.day.text = token

//        val apiService = token?.let { ApiClient.getClient(it).create(ApiService::class.java) }
//
//        apiService?.getAccountInfo(LocalDateTime.now(), CategoryType.ALL)
//            ?.enqueue(object : Callback<Map<Account, List<Operation>>> {
//            override fun onResponse(call: Call<Map<Account, List<Operation>>>, response: Response<Map<Account, List<Operation>>>) {
//                // accountname = response[1].account.name условно
//                // и тд
//                // вернее homeViewModel.setAccountName
//
//            }
//
//            override fun onFailure(call: Call<Map<Account, List<Operation>>>, t: Throwable) {
//                // обработка ошибки
//            }
//
//        })
        return root
    }

    class HomePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int {
            return 3
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> TabAllFragment()
                1 -> TabIncomeFragment()
                2 -> TabExpensesFragment()
                else -> throw IllegalArgumentException("Invalid position")
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateDateInView() {
        val myFormat = "d MMMM"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        binding.date.text = sdf.format(cal.time)
    }
}