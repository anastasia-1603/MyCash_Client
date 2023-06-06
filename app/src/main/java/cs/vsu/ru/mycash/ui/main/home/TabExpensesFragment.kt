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
import cs.vsu.ru.mycash.databinding.FragmentTabExpensesBinding

class TabExpensesFragment : Fragment() {
    private lateinit var binding: FragmentTabExpensesBinding
    private lateinit var adapter: OperationAdapter
    private val operationViewModel : OperationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTabExpensesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val manager = LinearLayoutManager(context)
        adapter = OperationAdapter(OperationAdapter.OnClickListener { operation ->
            Toast.makeText(activity, operation.comment, Toast.LENGTH_SHORT).show()
        })

        val expenseOperationsList = operationViewModel.expenseOperations.value
        Log.e("exp", operationViewModel.expenseOperations.value.toString())
        adapter.data = expenseOperationsList ?: emptyList()

        binding.recyclerView.layoutManager = manager
        binding.recyclerView.adapter = adapter
        binding.textView.text = expenseOperationsList?.let { sum(it).toString() }


        operationViewModel.expenseOperations.observe(viewLifecycleOwner) {
            adapter.data = it
        }
        return root
    }

    fun sum(list : List<Operation>) : Double
    {
        return list.sumOf { it.value }
    }
}