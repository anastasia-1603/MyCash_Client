package cs.vsu.ru.mycash.ui.main.diagrams

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import cs.vsu.ru.mycash.databinding.FragmentDiagramsIncomeBinding
import kotlin.streams.toList

class DiagramsIncomeFragment : Fragment() {

    private lateinit var binding: FragmentDiagramsIncomeBinding
    private val diagramsViewModel: DiagramsViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDiagramsIncomeBinding.inflate(inflater, container, false)

        configureChart()

        val barData = BarData(getChartData())
        binding.stackedBarChart.data = barData
        return binding.root
    }


    private fun getChartData(): BarDataSet {

        val dataList: ArrayList<BarEntry> = ArrayList()
        val data = diagramsViewModel.data.value
        var listC = emptyList<Int>()
        if (data != null) {
            val incData = data.incomes

            val map = incData.associate { dayCategoriesSum ->
                dayCategoriesSum.day to dayCategoriesSum.data.associate {
                    it.first to it.second
                }
            }

            listC = map.entries.first().value.entries.stream().map {
                it.key.color
            }.toList()

            map.entries.stream().map { entry ->
                val floatArray = entry.value.values.stream().map {
                    it.toFloat()
                }.toList().toTypedArray().toFloatArray()

                dataList.add(BarEntry(entry.key.toFloat(), floatArray))
            }
        }
        val barDataSet = BarDataSet(dataList, "Расходы")
        barDataSet.colors = listC
        return barDataSet
    }

    private fun configureChart() {
        binding.stackedBarChart.xAxis.setDrawGridLines(false)
        binding.stackedBarChart.description.isEnabled = false
    }


}