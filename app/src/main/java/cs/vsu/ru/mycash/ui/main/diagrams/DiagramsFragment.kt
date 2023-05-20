package cs.vsu.ru.mycash.ui.main.diagrams

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import cs.vsu.ru.mycash.R
import cs.vsu.ru.mycash.databinding.FragmentDiagramsBinding
import cs.vsu.ru.mycash.ui.main.diagrams.DiagramsViewModel

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
        val diagramsViewModel =
            ViewModelProvider(this).get(DiagramsViewModel::class.java)

        _binding = FragmentDiagramsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDiagrams
        diagramsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}