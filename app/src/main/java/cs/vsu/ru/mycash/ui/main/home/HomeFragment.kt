package cs.vsu.ru.mycash.ui.main.home

import android.app.DatePickerDialog
import android.content.Context
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
import android.widget.ToggleButton
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import by.dzmitry_lakisau.month_year_picker_dialog.MonthYearPickerDialog
import com.google.android.material.tabs.TabLayoutMediator
import cs.vsu.ru.mycash.R
import cs.vsu.ru.mycash.adapter.OperationAdapter
import cs.vsu.ru.mycash.api.ApiAuthClient
import cs.vsu.ru.mycash.api.ApiClient
import cs.vsu.ru.mycash.api.ApiService
import cs.vsu.ru.mycash.data.*
import cs.vsu.ru.mycash.databinding.FragmentHomeBinding
import cs.vsu.ru.mycash.service.OperationService
import retrofit2.Call
import java.text.SimpleDateFormat
import java.util.*
import retrofit2.Callback;
import retrofit2.Response;

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private var cal = Calendar.getInstance()
    private val dateFormat = "d MMMM"

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
            val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
            date.text = sdf.format(it.time)
        }

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                val sdf = SimpleDateFormat(dateFormat, Locale.getDefault())
                sdf.format(cal.time)
                homeViewModel.setDate(cal)
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

        val operationService = OperationService()


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
        Log.e("token2", token.toString())


        val apiService = token?.let { ApiAuthClient.getClient(it).create(ApiService::class.java) }

        homeViewModel.setAccountName("основа")

        val call = apiService?.getAccountInfo(
            "основа",
            2023,
            5,
            6)

        if (call != null) {
            call.enqueue(object : Callback<List<Operation>> {
                override fun onResponse(
                    call: Call<List<Operation>>,
                    response: Response<List<Operation>>
                ) {
                    response.body()?.let { homeViewModel.setOperationList(it) }
                    Log.e("op", response.body().toString())
                    val op = response.body()?.get(0)
                    val operations = operationService.operations
                    homeViewModel.setOperationList(operations)
                    homeViewModel.setExpenseList(operations.filter { it.category.type == CategoryType.EXPENSE })
                    homeViewModel.setIncomeList(operations.filter { it.category.type == CategoryType.INCOME })

                }

                override fun onFailure(call: Call<List<Operation>>, t: Throwable) {
                    val operations = operationService.operations
                    homeViewModel.setOperationList(operations)
                    homeViewModel.setExpenseList(operations.filter { it.category.type == CategoryType.EXPENSE })
                    homeViewModel.setIncomeList(operations.filter { it.category.type == CategoryType.INCOME })
                    Toast.makeText(context, "ошибка", Toast.LENGTH_SHORT).show()
                }
            })
        }

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

}

