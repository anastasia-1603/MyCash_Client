package cs.vsu.ru.mycash.ui.main.diagrams

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import cs.vsu.ru.mycash.data.diagrams.DayCategoriesSum
import cs.vsu.ru.mycash.databinding.FragmentDiagramsExpensesBinding
import kotlin.streams.toList

class DiagramsExpensesFragment : Fragment() {

    private lateinit var binding: FragmentDiagramsExpensesBinding
    private lateinit var diagramsExpensesViewModel: DiagramsExpensesViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentDiagramsExpensesBinding.inflate(inflater, container, false)

        val c = arrayListOf(Color.BLACK, Color.BLUE, Color.CYAN)

        val barDataSet = BarDataSet(loadData(), "Bar")
        barDataSet.colors = c
        val barData = BarData(barDataSet)
        binding.stackedBarChart.data = barData

        return binding.root
    }


    fun loadData(): ArrayList<BarEntry> {
        val dataVals: ArrayList<BarEntry> = ArrayList<BarEntry>()
        dataVals.add(BarEntry(0F, floatArrayOf(2.0F, 3.0F, 5.0F)))
        dataVals.add(BarEntry(3F, floatArrayOf(4.0F, 2.0F, 8.0F)))
        dataVals.add(BarEntry(5F, floatArrayOf(1.0F, 1.0F, 9.0F)))
        return dataVals
    }

    fun setData(dayCategoriesSumList: List<DayCategoriesSum>) {
//        val dataVals: ArrayList<BarEntry> = ArrayList<BarEntry>()
//        val barDataSets : List<ArrayList<BarEntry>> =
//            dayCategoriesSumList
//                .stream()
//                .map {
//
//                }

//        val dataByCategory = dayCategoriesSumList
//            .stream()
//            .map {
//                it.data
//            }.toList()
//
//        val colors : List<Int> = dayCategoriesSumList
//            .stream()
//            .map {
//                it.category.color
//            }.toList()
//
//        val entries : List<>
//
//        for (dcs in dayCategoriesSumList)
//        {
//            val category = dcs.category
//            val color = category.color
//
//        }
//
//        val barDataSet: BarDataSet = BarDataSet(loadData(), "BarSet")
//        barDataSet.colors = colors


//        val dataVals: ArrayList<BarEntry> = ArrayList<BarEntry>()
//        val data: List<FloatArray> = dayCategoriesSumList
//            .stream()
//            .map { dcs ->
//                dcs.data
//                    .stream()
//                    .map {
//                        it.second.toFloat()
//                    }.toList().toFloatArray()
//            }.toList()
//
//
//        val colors: List<Int> = dayCategoriesSumList
//            .stream()
//            .map {dcs ->
//                dcs.data.stream().map {
//                    val category = it.first
//                    val color = it.first.color
//
//                }
//            }.



    }

}