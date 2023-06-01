package cs.vsu.ru.mycash.ui.main.diagrams

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
import cs.vsu.ru.mycash.databinding.FragmentDiagramsBinding
import cs.vsu.ru.mycash.ui.main.diagrams.DiagramsViewModel
import cs.vsu.ru.mycash.ui.main.home.HomeFragment
import cs.vsu.ru.mycash.ui.main.home.TabAllFragment
import cs.vsu.ru.mycash.ui.main.home.TabExpensesFragment
import cs.vsu.ru.mycash.ui.main.home.TabIncomeFragment

class DiagramsFragment : Fragment() {

    private var _binding: FragmentDiagramsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val diagramsViewModel = ViewModelProvider(this)[DiagramsViewModel::class.java]

        _binding = FragmentDiagramsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        val viewPager = binding.viewPager
        viewPager.adapter = DiagramsPagerAdapter(this)
        val tabLayoutMediator =
            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = "Общий"
                    1 -> tab.text = "Доходы"
                    2 -> tab.text = "Расходы"
                }
            }
        tabLayoutMediator.attach()

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class DiagramsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int {
            return 3
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> DiagramsAllFragment()
                1 -> DiagramsIncomeFragment()
                2 -> DiagramsExpensesFragment()
                else -> throw IllegalArgumentException("Invalid position")
            }
        }
    }


}