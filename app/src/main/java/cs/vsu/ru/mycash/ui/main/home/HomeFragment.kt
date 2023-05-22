package cs.vsu.ru.mycash.ui.main.home

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cs.vsu.ru.mycash.adapter.OperationAdapter
import cs.vsu.ru.mycash.api.ApiClient
import cs.vsu.ru.mycash.api.ApiService
import cs.vsu.ru.mycash.data.*
import cs.vsu.ru.mycash.databinding.FragmentHomeBinding
import cs.vsu.ru.mycash.service.OperationService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDateTime

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var adapter: OperationAdapter
    private lateinit var operationService: OperationService

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

//        homeViewModel.setBalance("1000") //test
//        homeViewModel.setAccountName("Новый счет") //test

        val manager = LinearLayoutManager(context)
        adapter = OperationAdapter(OperationAdapter.OnClickListener { operation ->
            Toast.makeText(activity, operation.category.name, Toast.LENGTH_SHORT).show()
        })
        operationService = OperationService() //test
        adapter.data = operationService.operations //test
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.adapter = adapter
        binding.accountName.text = operationService.operations[1].account.name
        binding.balance.text = operationService.operations[1].account.balance.toString()

        val accountName: TextView = binding.accountName
        homeViewModel.accountName.observe(viewLifecycleOwner) {
            accountName.text = it
        }

        val balance: TextView = binding.balance
        homeViewModel.balance.observe(viewLifecycleOwner) {
            balance.text = it
        }

        val all = binding.all
        val income = binding.income
        val expenses = binding.expenses

        all.setOnClickListener{
            Toast.makeText(context, "text", Toast.LENGTH_SHORT).show()
        }
        income.setOnClickListener{
            Toast.makeText(context, "text", Toast.LENGTH_SHORT).show()
        }
        expenses.setOnClickListener{
            Toast.makeText(context, "text", Toast.LENGTH_SHORT).show()
        }

        val preferences = activity?.getSharedPreferences("MY_APP", Context.MODE_PRIVATE)
        val token = preferences?.getString("TOKEN", "token")
        binding.day.text = token

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}