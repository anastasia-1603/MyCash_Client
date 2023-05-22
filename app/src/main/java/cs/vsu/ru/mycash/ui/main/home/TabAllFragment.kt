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
import cs.vsu.ru.mycash.databinding.FragmentHomeBinding
import cs.vsu.ru.mycash.databinding.FragmentTabAllBinding
import cs.vsu.ru.mycash.service.OperationService


class TabAllFragment : Fragment() {
    private lateinit var binding: FragmentTabAllBinding
    private lateinit var adapter: OperationAdapter
    private lateinit var operationService: OperationService

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTabAllBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val manager = LinearLayoutManager(context)
        adapter = OperationAdapter(OperationAdapter.OnClickListener { operation ->
            Toast.makeText(activity, operation.category.name, Toast.LENGTH_SHORT).show()
        })
        operationService = OperationService() //test
        adapter.data = operationService.operations //test
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.adapter = adapter

        return root
    }

}