package cs.vsu.ru.mycash.ui.main.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import cs.vsu.ru.mycash.databinding.FragmentCategoriesIncomeBinding

class CategoriesIncomeFragment : Fragment() {
    private lateinit var binding: FragmentCategoriesIncomeBinding
    private lateinit var adapter: CategoriesAdapter
    private val categoriesViewModel : CategoriesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentCategoriesIncomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val manager = LinearLayoutManager(context)
        adapter = CategoriesAdapter(CategoriesAdapter.OnClickListener { category ->
            //todo findNavController().navigate()
        })

        val incomeCategoriesList = categoriesViewModel.incomeCategories.value
        adapter.data = incomeCategoriesList ?: emptyList()

        binding.recyclerViewIncome.layoutManager = manager
        binding.recyclerViewIncome.adapter = adapter


        categoriesViewModel.incomeCategories.observe(viewLifecycleOwner) {
            adapter.data = it
        }
        return root
    }

}