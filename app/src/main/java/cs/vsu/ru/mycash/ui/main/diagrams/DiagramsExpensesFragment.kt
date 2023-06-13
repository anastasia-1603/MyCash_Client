package cs.vsu.ru.mycash.ui.main.diagrams

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import cs.vsu.ru.mycash.databinding.FragmentDiagramsExpensesBinding
import kotlin.streams.toList


class DiagramsExpensesFragment : Fragment() {

    private lateinit var binding: FragmentDiagramsExpensesBinding
    private val diagramsViewModel: DiagramsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDiagramsExpensesBinding.inflate(inflater, container, false)

        diagramsViewModel.data.observe(viewLifecycleOwner) {
            binding.stackedBarChart.notifyDataSetChanged()
            binding.stackedBarChart.invalidate()
            setChartData()
        }
        return binding.root
    }


    private fun setChartData() {
        configureChart()
        val barData = BarData(getChartData())
        binding.stackedBarChart.data = barData
    }

    private fun getChartData(): BarDataSet {

        val dataList: ArrayList<BarEntry> = ArrayList()
        val data = diagramsViewModel.data.value
        var listC = emptyList<Int>()
        var listNames: Array<String>? = null
        if (data != null) {
            val expData = data.expenses

            val map = expData.associate { dayCategoriesSum ->
                dayCategoriesSum.day to dayCategoriesSum.data.associate {
                    it.first to it.second
                }
            }

            listC = map.entries.first().value.entries.stream().map {
                it.key.color
            }.toList()

            listNames = map.entries.first().value.entries.stream().map {
                it.key.name
            }.toList().toTypedArray()

            for (entry in map.entries) {
                val floatList = ArrayList<Float>()
                for (sum in entry.value.values) {
                    floatList.add(sum.toFloat())
                }
                val floatArray = floatList.toFloatArray()

                dataList.add(BarEntry(entry.key.toFloat(), floatArray))
            }

        }
        val barDataSet = BarDataSet(dataList, "")
        barDataSet.colors = listC
        barDataSet.setDrawValues(false)
        barDataSet.stackLabels = listNames

        return barDataSet
    }

    private fun configureChart() {
        val chart = binding.stackedBarChart
        chart.xAxis.setDrawGridLines(false)
        chart.description.isEnabled = false
        chart.legend.isWordWrapEnabled = true
    }

}