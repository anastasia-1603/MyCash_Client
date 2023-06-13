package cs.vsu.ru.mycash.ui.main.diagrams

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import cs.vsu.ru.mycash.data.diagrams.AllMonthData
import cs.vsu.ru.mycash.data.diagrams.AnalyticsResponse
import cs.vsu.ru.mycash.databinding.FragmentDiagramsAllBinding
import java.util.*

class DiagramsAllFragment : Fragment() {

    private lateinit var binding: FragmentDiagramsAllBinding
    private val diagramsViewModel: DiagramsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDiagramsAllBinding.inflate(inflater, container, false)

        diagramsViewModel.data.observe(viewLifecycleOwner) {
            binding.lineChart.notifyDataSetChanged()
            binding.lineChart.invalidate()
            setLineChartData(it)
        }

        return binding.root
    }

    private fun setLineChartData(analyticsResp: AnalyticsResponse?) {
        configureChart()
        val allMonthData: AllMonthData
        val xValues = getXValues()

        if (analyticsResp != null) {
            allMonthData = analyticsResp.all
            val balanceList = allMonthData.balanceMonths as ArrayList<Double>
            val expenses = allMonthData.expenses as ArrayList<Double>
            val incomes = allMonthData.incomes as ArrayList<Double>
            val target = allMonthData.target
            val limit = allMonthData.accountLimit

            val size = allMonthData.balanceMonths.size

            val finalDataSet = ArrayList<LineDataSet>()
            finalDataSet.add(getBalanceDataSet(balanceList))
            finalDataSet.add(getIncomesDataSet(incomes))
            finalDataSet.add(getExpensesDataSet(expenses))
            if (target != 0.0) {
                finalDataSet.add(getTargetDataSet(target, size))
            }
            if (limit != 0.0) {
                finalDataSet.add(getLimitDataSet(limit, size))
            }
            val lineData = LineData(finalDataSet as List<ILineDataSet>?)
            binding.lineChart.xAxis.valueFormatter = IndexAxisValueFormatter(xValues)
            binding.lineChart.data = lineData
        }
    }

    private fun configureChart() {
        binding.lineChart.xAxis.setDrawGridLines(false)
        binding.lineChart.description.isEnabled = false
        binding.lineChart.legend.isWordWrapEnabled = true
    }

    private fun getBalanceDataSet(balanceList : List<Double>) : LineDataSet {

        val bList = balanceList.reversed()
        val lineList = ArrayList<Entry>()
        for ((i, b) in bList.withIndex()) {
            lineList.add(Entry(i.toFloat(), b.toFloat()))
        }
//        lineList.reverse()
        val lineDataSet = LineDataSet(lineList, "Баланс")
        lineDataSet.lineWidth = 3f
        lineDataSet.setDrawValues(false)
        lineDataSet.color = Color.BLUE
        return lineDataSet
    }

    private fun getXValues() : ArrayList<String>
    {
        val curMonth = Calendar.getInstance().get(Calendar.MONTH)
        val curYear = Calendar.getInstance().get(Calendar.YEAR)
        val monthNames = arrayOf(
            "Янв",
            "Фев",
            "Март",
            "Апр",
            "Май",
            "Июн",
            "Июл",
            "Авг",
            "Сент",
            "Окт",
            "Нояб",
            "Дек"
        )
        val pastMonth = ArrayList<String>()
        var cur = curMonth - 1
        for (i in 1..6) {
            pastMonth.add(0, monthNames[cur])
            if (cur == 0) {
                cur = monthNames.size - 1
            } else {
                cur--
            }
        }
        return pastMonth
    }

    private fun getTargetDataSet(target: Double, size: Int) : LineDataSet{
        val lineList = ArrayList<Entry>()
        for (i in 1 .. 6)
        {
            lineList.add(Entry(i.toFloat(), target.toFloat()))
        }
        val lineDataSet = LineDataSet(lineList, "Цель")
        lineDataSet.lineWidth = 3f
        lineDataSet.valueTextSize = 15f
        lineDataSet.setDrawValues(false)
        lineDataSet.color = Color.CYAN
        return lineDataSet
    }

    private fun getLimitDataSet(limit: Double, size: Int) : LineDataSet {
        val lineList = ArrayList<Entry>()
        for (i in 1 .. 6)
        {
            lineList.add(Entry(i.toFloat(), limit.toFloat()))
        }
        val lineDataSet = LineDataSet(lineList, "Лимит")
        lineDataSet.lineWidth = 3f
        lineDataSet.valueTextSize = 15f
        lineDataSet.setDrawValues(false)
        lineDataSet.color = Color.YELLOW
        return lineDataSet
    }

    private fun getExpensesDataSet(expenses: List<Double>) : LineDataSet {
        val lineList = ArrayList<Entry>()
        for ((i, b) in expenses.withIndex()) {
            lineList.add(Entry((i+1).toFloat(), b.toFloat()))
        }
        val lineDataSet = LineDataSet(lineList, "Расходы")
        lineDataSet.lineWidth = 3f
        lineDataSet.valueTextSize = 15f
        lineDataSet.setDrawValues(false)
        lineDataSet.color = Color.RED
        return lineDataSet
    }

    private fun getIncomesDataSet(incomes: List<Double>) : LineDataSet {
        val lineList = ArrayList<Entry>()
        for ((i, b) in incomes.withIndex()) {
            lineList.add(Entry((i+1).toFloat(), b.toFloat()))
        }
        val lineDataSet = LineDataSet(lineList, "Доходы")
        lineDataSet.lineWidth = 3f
        lineDataSet.valueTextSize = 15f
        lineDataSet.setDrawValues(false)
        lineDataSet.color = Color.GREEN
        return lineDataSet
    }

}
