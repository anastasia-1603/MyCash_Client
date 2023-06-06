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
import cs.vsu.ru.mycash.api.ApiService
import cs.vsu.ru.mycash.databinding.FragmentDiagramsExpensesBinding
import cs.vsu.ru.mycash.ui.main.categories.CategoriesViewModel
import cs.vsu.ru.mycash.utils.AppPreferences
import kotlin.streams.toList

class DiagramsExpensesFragment : Fragment() {

    private lateinit var binding: FragmentDiagramsExpensesBinding
    private lateinit var diagramsExpensesViewModel: DiagramsExpensesViewModel
    private lateinit var apiService: ApiService
    private val categoriesViewModel: CategoriesViewModel by activityViewModels()
    private lateinit var appPrefs: AppPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        appPrefs = activity?.let { AppPreferences(it) }!!
        binding = FragmentDiagramsExpensesBinding.inflate(inflater, container, false)

        val listC: List<Int> = categoriesViewModel.expenseCategories.value!!
            .stream()
            .map {
                -it.color
            }.toList()

//        val listL: List<String> = categoriesViewModel.expenseCategories.value!!
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
        val dataVals: ArrayList<BarEntry> = ArrayList<BarEntry>()
        dataVals.add(BarEntry(0F, floatArrayOf(250.0F, 300.0F, 25.0F, 250.0F, 3.0F, 0.0F, 0.0F, 0.0F, 80.0F, 100.0F)))
        dataVals.add(BarEntry(1F, floatArrayOf(0.0F, 280.0F, 25.0F, 250.0F, 3.0F, 0.0F, 0.0F, 0.0F, 80.0F, 100.0F)))
        dataVals.add(BarEntry(3F, floatArrayOf(250.0F, 300.0F, 100.0F, 250.0F, 3.0F, 0.0F, 0.0F, 0.0F, 80.0F, 100.0F)))
        dataVals.add(BarEntry(9F, floatArrayOf(0.0F, 300.0F, 25.0F, 250.0F, 1800.0F, 0.0F, 0.0F, 0.0F, 80.0F, 100.0F)))
        dataVals.add(BarEntry(16F, floatArrayOf(250.0F, 300.0F, 25.0F, 250.0F, 3.0F, 0.0F, 0.0F, 0.0F, 80.0F, 100.0F)))
        dataVals.add(BarEntry(17F, floatArrayOf(250.0F, 3000.0F, 25.0F, 250.0F, 3.0F, 0.0F, 0.0F, 0.0F, 80.0F, 100.0F)))
        dataVals.add(BarEntry(25F, floatArrayOf(0.0F, 0.0F, 0.0F, 50.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F)))
        dataVals.add(BarEntry(28F, floatArrayOf(0.0F, 30.0F, 600.0F, 250.0F, 3.0F, 0.0F, 900.0F, 0.0F, 80.0F, 100.0F)))
        dataVals.add(BarEntry(30F, floatArrayOf(250.0F, 300.0F, 25.0F, 700.0F, 3.0F, 0.0F, 0.0F, 0.0F, 80.0F, 100.0F)))
        return dataVals
    }





}