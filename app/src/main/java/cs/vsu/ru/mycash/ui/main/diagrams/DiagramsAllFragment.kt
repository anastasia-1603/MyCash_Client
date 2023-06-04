package cs.vsu.ru.mycash.ui.main.diagrams

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cs.vsu.ru.mycash.R

class DiagramsAllFragment : Fragment() {

    companion object {
        fun newInstance() = DiagramsAllFragment()
    }

    private lateinit var viewModel: DiagramsAllViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_diagrams_all, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DiagramsAllViewModel::class.java)
        // TODO: Use the ViewModel
    }

}