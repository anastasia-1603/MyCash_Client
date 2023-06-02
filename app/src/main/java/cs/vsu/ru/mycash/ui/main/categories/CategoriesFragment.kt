package cs.vsu.ru.mycash.ui.main.categories

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import cs.vsu.ru.mycash.R
import cs.vsu.ru.mycash.databinding.FragmentCategoriesBinding
import cs.vsu.ru.mycash.ui.main.categories.CategoriesViewModel
import cs.vsu.ru.mycash.ui.main.diagrams.DiagramsAllFragment
import cs.vsu.ru.mycash.ui.main.diagrams.DiagramsExpensesFragment
import cs.vsu.ru.mycash.ui.main.diagrams.DiagramsFragment
import cs.vsu.ru.mycash.ui.main.diagrams.DiagramsIncomeFragment

class CategoriesFragment : Fragment() {
    private var _binding: FragmentCategoriesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val categoriesViewModel =
            ViewModelProvider(this).get(CategoriesViewModel::class.java)

        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val viewPager = binding.viewPager
        viewPager.adapter = CategoriesPagerAdapter(this)
        val tabLayoutMediator =
            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = "Доходы"
                    1 -> tab.text = "Расходы"
                }
            }
        tabLayoutMediator.attach()
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class CategoriesPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int {
            return 2
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> CategoriesIncomeFragment()
                1 -> CategoriesExpensesFragment()
                else -> throw IllegalArgumentException("Invalid position")
            }
        }
    }

}