package cs.vsu.ru.mycash.ui.main.diagrams

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cs.vsu.ru.mycash.R

class DiagramsIncomeFragment : Fragment() {

    companion object {
        fun newInstance() = DiagramsIncomeFragment()
    }

    private lateinit var viewModel: DiagramsIncomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_diagrams_income, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DiagramsIncomeViewModel::class.java)
        // TODO: Use the ViewModel
    }

}