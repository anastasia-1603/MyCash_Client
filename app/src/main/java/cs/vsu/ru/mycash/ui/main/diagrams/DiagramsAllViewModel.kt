package cs.vsu.ru.mycash.ui.main.diagrams

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cs.vsu.ru.mycash.data.diagrams.AllMonthData
import cs.vsu.ru.mycash.data.diagrams.DayCategoriesSum

class DiagramsAllViewModel : ViewModel() {
    private val _data: MutableLiveData<AllMonthData> by lazy {
        MutableLiveData<AllMonthData>()
    }

    val data : LiveData<AllMonthData> = _data

    fun setData(data: AllMonthData)
    {
        _data.value = data
    }
}