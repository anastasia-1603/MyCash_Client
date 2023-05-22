package cs.vsu.ru.mycash.ui.main.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import cs.vsu.ru.mycash.R
import cs.vsu.ru.mycash.adapter.OperationAdapter
import cs.vsu.ru.mycash.databinding.FragmentTabAllBinding
import cs.vsu.ru.mycash.databinding.FragmentTabExpensesBinding
import cs.vsu.ru.mycash.service.OperationService


class TabExpensesFragment : Fragment() {
    private lateinit var binding: FragmentTabExpensesBinding
    private lateinit var adapter: OperationAdapter
    private lateinit var operationService: OperationService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTabExpensesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val manager = LinearLayoutManager(context)
        adapter = OperationAdapter(OperationAdapter.OnClickListener { operation ->
            Toast.makeText(activity, operation.category.name, Toast.LENGTH_SHORT).show()
        })
        operationService = OperationService() //test
        adapter.data = operationService.operations //test
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.adapter = adapter
        val text = "1000"
        binding.textView.text = text
        return root
    }
}