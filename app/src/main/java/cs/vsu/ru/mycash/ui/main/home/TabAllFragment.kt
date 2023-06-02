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
import cs.vsu.ru.mycash.databinding.FragmentTabAllBinding
//import cs.vsu.ru.mycash.service.OperationService


class TabAllFragment : Fragment() {
    private lateinit var binding: FragmentTabAllBinding
    private lateinit var adapter: OperationAdapter
    private val operationViewModel: OperationViewModel by activityViewModels()

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

//        val operationViewModel = ViewModelProvider(requireActivity())[OperationViewModel::class.java]


        val operationList = operationViewModel.operationList.value
        Log.e("taball", operationViewModel.operationList.value.toString())

//        val operationList =  OperationService().operations
        adapter.data = operationList ?: emptyList()
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.adapter = adapter

        operationViewModel.operationList.observe(viewLifecycleOwner) {
            adapter.data = it
        }

        return root
    }

}