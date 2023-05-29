package cs.vsu.ru.mycash.ui.main.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cs.vsu.ru.mycash.R
import cs.vsu.ru.mycash.adapter.OperationAdapter
import cs.vsu.ru.mycash.data.CategoryType
import cs.vsu.ru.mycash.data.Operation
import cs.vsu.ru.mycash.databinding.FragmentTabExpensesBinding
import cs.vsu.ru.mycash.databinding.FragmentTabIncomeBinding
//import cs.vsu.ru.mycash.service.OperationService


class TabIncomeFragment : Fragment() {
    private lateinit var binding: FragmentTabIncomeBinding
    private lateinit var adapter: OperationAdapter
    private val operationViewModel : OperationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

//        val operationViewModel = ViewModelProvider(requireActivity())[OperationViewModel::class.java]

        binding = FragmentTabIncomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val manager = LinearLayoutManager(context)
        adapter = OperationAdapter(OperationAdapter.OnClickListener { operation ->
            Toast.makeText(activity, operation.category.name, Toast.LENGTH_SHORT).show()
        })

        val incomeOperationsList = operationViewModel.incomeOperations.value
        Log.e("inc", operationViewModel.incomeOperations.value.toString())
//        val operationsList =  OperationService().operations

        adapter.data = incomeOperationsList ?: emptyList()


        binding.recyclerView.layoutManager = manager
        binding.recyclerView.adapter = adapter
        //val text = "1000"
        binding.textView.text = incomeOperationsList?.let { sum(it).toString() }



        operationViewModel.incomeOperations.observe(viewLifecycleOwner) {
            adapter.data = it
        }
//        homeViewModel.incomeOperations.observe(viewLifecycleOwner) {
//            adapter.data = it ?: emptyList()
//            adapter.notifyDataSetChanged()
//        }

        return root
    }

    fun sum(list : List<Operation>) : Double
    {
        return list.sumOf { it.value }
    }
}