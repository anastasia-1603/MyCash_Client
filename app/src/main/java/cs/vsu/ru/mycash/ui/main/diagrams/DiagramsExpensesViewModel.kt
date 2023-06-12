package cs.vsu.ru.mycash.ui.main.diagrams

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cs.vsu.ru.mycash.data.Account
import cs.vsu.ru.mycash.data.diagrams.DayCategoriesSum

class DiagramsExpensesViewModel : ViewModel() {
    private val _dataList: MutableLiveData<List<DayCategoriesSum>> by lazy {
        MutableLiveData<List<DayCategoriesSum>>()
    }

    val dataList : LiveData<List<DayCategoriesSum>> = _dataList

    fun setDataList(dataList: List<DayCategoriesSum>)
    {
        _dataList.value = dataList
    }

}