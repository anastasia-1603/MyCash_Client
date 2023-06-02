package cs.vsu.ru.mycash.ui.main.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import cs.vsu.ru.mycash.databinding.FragmentCategoriesExpensesBinding

class CategoriesExpensesFragment : Fragment() {
    private lateinit var binding: FragmentCategoriesExpensesBinding
    private lateinit var adapter: CategoriesAdapter
    private val categoriesViewModel : CategoriesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentCategoriesExpensesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val manager = LinearLayoutManager(context)
        adapter = CategoriesAdapter(CategoriesAdapter.OnClickListener { category ->
            Toast.makeText(activity, category.name, Toast.LENGTH_SHORT).show()
        })

        val expenseCategoriesList = categoriesViewModel.expenseCategories.value
        adapter.data = expenseCategoriesList ?: emptyList()

        binding.recyclerView2.layoutManager = manager
        binding.recyclerView2.adapter = adapter


        categoriesViewModel.expenseCategories.observe(viewLifecycleOwner) {
            adapter.data = it
        }
        return root
    }
}