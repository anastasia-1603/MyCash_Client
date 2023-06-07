package cs.vsu.ru.mycash.ui.main.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cs.vsu.ru.mycash.data.Account
import cs.vsu.ru.mycash.data.Category

class EditCategoryViewModel : ViewModel() {
    private val _category: MutableLiveData<Category> by lazy {
        MutableLiveData<Category>()
    }
    val category : LiveData<Category> = _category

    fun setCategory(category: Category)
    {
        _category.value = category
    }
}