package cs.vsu.ru.mycash.ui.main.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import cs.vsu.ru.mycash.utils.OperationAdapter
import cs.vsu.ru.mycash.data.Operation
import cs.vsu.ru.mycash.databinding.FragmentTabIncomeBinding

class TabIncomeFragment : Fragment() {
    private lateinit var binding: FragmentTabIncomeBinding
    private lateinit var adapter: OperationAdapter
    private val operationViewModel : OperationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTabIncomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val manager = LinearLayoutManager(context)
        adapter = OperationAdapter(OperationAdapter.OnClickListener { operation ->
            Toast.makeText(activity, operation.comment, Toast.LENGTH_SHORT).show()
        })

        val incomeOperationsList = operationViewModel.incomeOperations.value
        Log.e("inc", operationViewModel.incomeOperations.value.toString())

        adapter.data = incomeOperationsList ?: emptyList()


        binding.recyclerView.layoutManager = manager
        binding.recyclerView.adapter = adapter
        binding.textView.text = incomeOperationsList?.let { sum(it).toString() }


        operationViewModel.incomeOperations.observe(viewLifecycleOwner) {
            adapter.data = it
        }
        return root
    }

    fun sum(list : List<Operation>) : Double
    {
        return list.sumOf { it.value }
    }
}