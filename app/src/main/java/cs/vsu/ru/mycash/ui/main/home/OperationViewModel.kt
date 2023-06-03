package cs.vsu.ru.mycash.ui.main.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cs.vsu.ru.mycash.api.ApiService
import cs.vsu.ru.mycash.data.Account
import cs.vsu.ru.mycash.data.Operation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class OperationViewModel : ViewModel() {


    private val _map: MutableLiveData<Map<Account, List<Operation>>> by lazy {
        MutableLiveData<Map<Account, List<Operation>>>()
    }
    val map: LiveData<Map<Account, List<Operation>>> = _map

    fun setMap(map: Map<Account, List<Operation>>) {
        _map.value = map
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