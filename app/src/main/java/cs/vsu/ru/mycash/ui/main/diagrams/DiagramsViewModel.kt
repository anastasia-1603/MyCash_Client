package cs.vsu.ru.mycash.ui.main.diagrams

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DiagramsViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is diagrams Fragment"
    }
    val text: LiveData<String> = _text
}