package cs.vsu.ru.mycash.ui.main.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import cs.vsu.ru.mycash.R
import cs.vsu.ru.mycash.databinding.FragmentCategoriesExpensesBinding

class CategoriesExpensesFragment : Fragment() {
    private lateinit var binding: FragmentCategoriesExpensesBinding
    private lateinit var adapter: CategoriesAdapter
    private val categoriesViewModel : CategoriesViewModel by activityViewModels()
    private val editCategoryViewModel: EditCategoryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentCategoriesExpensesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val manager = LinearLayoutManager(context)
        adapter = CategoriesAdapter(CategoriesAdapter.OnClickListener { category ->
            editCategoryViewModel.setCategory(category)
            findNavController().navigate(R.id.editCategoryFragment)
        })

        val expenseCategoriesList = categoriesViewModel.expenseCategories.value
        adapter.data = expenseCategoriesList ?: emptyList()

        binding.recyclerViewExpenses.layoutManager = manager
        binding.recyclerViewExpenses.adapter = adapter


        categoriesViewModel.expenseCategories.observe(viewLifecycleOwner) {
            adapter.data = it
        }
        return root
    }
}