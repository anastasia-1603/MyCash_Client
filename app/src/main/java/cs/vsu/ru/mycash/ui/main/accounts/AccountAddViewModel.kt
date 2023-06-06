package cs.vsu.ru.mycash.ui.main.accounts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AccountAddViewModel : ViewModel() {
    private val _goal: MutableLiveData<Double> by lazy {
        MutableLiveData<Double>()
    }
    val balance: LiveData<Double> = _goal

    fun setGoal(goal: Double) {
        _goal.value = goal
    }

    private val _limit: MutableLiveData<Double> by lazy {
        MutableLiveData<Double>()
    }
    val limit: LiveData<Double> = _limit

    fun setLimit(limit: Double) {
        _limit.value = limit
    }
}