package cs.vsu.ru.mycash.ui.main.operation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cs.vsu.ru.mycash.data.MainScreenAccountResponse
import cs.vsu.ru.mycash.data.Operation

class OperationViewModel : ViewModel() {


    private val _data: MutableLiveData<List<MainScreenAccountResponse>> by lazy {
        MutableLiveData<List<MainScreenAccountResponse>>()
    }
    val data: LiveData<List<MainScreenAccountResponse>> = _data

    fun setData(data: List<MainScreenAccountResponse>) {
        _data.value = data
    }

    private val _operationList: MutableLiveData<List<Operation>> by lazy {
        MutableLiveData<List<Operation>>()
    }
    val operationList: LiveData<List<Operation>> = _operationList

    private val _incomeOperations: MutableLiveData<List<Operation>> by lazy {
        MutableLiveData<List<Operation>>()
    }
    val incomeOperations: LiveData<List<Operation>> = _incomeOperations

    private val _expenseOperations: MutableLiveData<List<Operation>> by lazy {
        MutableLiveData<List<Operation>>()
    }
    val expenseOperations: LiveData<List<Operation>> = _expenseOperations


    fun setOperationList(operations: List<Operation>) {
        _operationList.value = operations
    }

    fun setIncomeList(operations: List<Operation>) {
        _incomeOperations.value = operations
    }

    fun setExpenseList(operations: List<Operation>) {
        _expenseOperations.value = operations
    }

}