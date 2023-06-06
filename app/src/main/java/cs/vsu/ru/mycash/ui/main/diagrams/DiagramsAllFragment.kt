package cs.vsu.ru.mycash.ui.main.diagrams

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cs.vsu.ru.mycash.R
import cs.vsu.ru.mycash.databinding.FragmentDiagramsAllBinding

class DiagramsAllFragment : Fragment() {

    private lateinit var binding: FragmentDiagramsAllBinding
    private lateinit var diagramsAllViewModel: DiagramsAllViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDiagramsAllBinding.inflate(inflater, container, false)

        return binding.root
    }

    private fun setLineChartData()
    {
//        val xvalue = ArrayList<String>()
//        xvalue.add()
    }
}
