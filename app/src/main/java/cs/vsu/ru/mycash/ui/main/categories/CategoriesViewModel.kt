package cs.vsu.ru.mycash.ui.main.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cs.vsu.ru.mycash.data.Category

class CategoriesViewModel : ViewModel() {


    private val _categoryList: MutableLiveData<List<Category>> by lazy {
        MutableLiveData<List<Category>>()
    }
    val categoryList: LiveData<List<Category>> = _categoryList

    private val _incomeCategories: MutableLiveData<List<Category>> by lazy {
        MutableLiveData<List<Category>>()
    }
    val incomeCategories: LiveData<List<Category>> = _incomeCategories

    private val _expenseCategories: MutableLiveData<List<Category>> by lazy {
        MutableLiveData<List<Category>>()
    }
    val expenseCategories: LiveData<List<Category>> = _expenseCategories


    fun setCategoryList(categories: List<Category>) {
        _categoryList.value = categories
    }

    fun setIncomeList(categories: List<Category>) {
        _incomeCategories.value = categories
    }

    fun setExpenseList(categories: List<Category>) {
        _expenseCategories.value = categories
    }

}