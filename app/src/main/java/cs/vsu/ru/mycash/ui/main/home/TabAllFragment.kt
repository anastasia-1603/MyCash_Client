package cs.vsu.ru.mycash.ui.main.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import cs.vsu.ru.mycash.R
import cs.vsu.ru.mycash.adapter.OperationAdapter
import cs.vsu.ru.mycash.databinding.FragmentHomeBinding
import cs.vsu.ru.mycash.databinding.FragmentTabAllBinding
import cs.vsu.ru.mycash.service.OperationService


class TabAllFragment : Fragment() {
    private lateinit var binding: FragmentTabAllBinding
    private lateinit var adapter: OperationAdapter
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

        val homeViewModel = ViewModelProvider(requireActivity())[HomeViewModel::class.java]
        val operationsList = homeViewModel.operationList.value
        if (operationsList != null) {
            adapter.data = operationsList
        }
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.adapter = adapter

        homeViewModel.operationList.observe(viewLifecycleOwner) {
            adapter.notifyDataSetChanged()
        }

        return root
    }

}