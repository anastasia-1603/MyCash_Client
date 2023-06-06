package cs.vsu.ru.mycash.ui.main.diagrams

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import cs.vsu.ru.mycash.R
import cs.vsu.ru.mycash.api.ApiService
import cs.vsu.ru.mycash.databinding.FragmentDiagramsExpensesBinding
import cs.vsu.ru.mycash.databinding.FragmentDiagramsIncomeBinding
import cs.vsu.ru.mycash.ui.main.categories.CategoriesViewModel
import cs.vsu.ru.mycash.utils.AppPreferences
import kotlin.streams.toList

class DiagramsIncomeFragment : Fragment() {

    private lateinit var binding: FragmentDiagramsIncomeBinding
    private lateinit var diagramsIncomeViewModel: DiagramsIncomeViewModel
    private lateinit var apiService: ApiService
    private val categoriesViewModel: CategoriesViewModel by activityViewModels()
    private lateinit var appPrefs: AppPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        appPrefs = activity?.let { AppPreferences(it) }!!
        binding = FragmentDiagramsIncomeBinding.inflate(inflater, container, false)

        val listC: List<Int> = categoriesViewModel.incomeCategories.value!!
            .stream()
            .map {
                -it.color
            }.toList()

//        val listL: List<String> = categoriesViewModel.incomeCategories.value!!
//            .stream()
//            .map {
//                it.name.toString()
//            }.toList()

        val barDataSet = BarDataSet(loadData(), "График")
//        barDataSet.stackLabels = listL.toTypedArray()
        barDataSet.colors = listC
        val barData = BarData(barDataSet)
        binding.stackedBarChart.data = barData

        return binding.root
    }


    fun loadData(): ArrayList<BarEntry> {
        val dataVals: ArrayList<BarEntry> = ArrayList()

        dataVals.add(BarEntry(0F, floatArrayOf( 0.0F, 0.0F, 0.0F, 80.0F, 100.0F, 400.0F)))
        dataVals.add(BarEntry(1F, floatArrayOf( 180.0F, 0.0F, 0.0F, 80.0F, 100.0F, 400.0F)))
        dataVals.add(BarEntry(3F, floatArrayOf( 0.0F, 0.0F, 0.0F, 80.0F, 10700.0F, 400.0F)))
        dataVals.add(BarEntry(9F, floatArrayOf( 647.0F, 0.0F, 1090.0F, 80.0F, 100.0F, 400.0F)))
        dataVals.add(BarEntry(16F, floatArrayOf( 23.0F, 0.0F, 0.0F, 80.0F, 100.0F, 400.0F)))
        dataVals.add(BarEntry(17F, floatArrayOf( 0.0F, 0.0F, 0.0F, 80.0F, 100.0F, 400.0F)))
        dataVals.add(BarEntry(25F, floatArrayOf( 0.0F, 0.0F, 2389.0F, 80.0F, 100.0F, 400.0F)))
        dataVals.add(BarEntry(28F, floatArrayOf( 10000.0F, 0.0F, 0.0F, 7000.0F, 100.0F, 400.0F)))
        dataVals.add(BarEntry(30F, floatArrayOf( 0.0F, 0.0F, 0.0F, 80.0F, 100.0F, 400.0F)))
        return dataVals
    }


}