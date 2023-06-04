package cs.vsu.ru.mycash.ui.main.home

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import cs.vsu.ru.mycash.R

class PredictFragment : Fragment() {

    companion object {
        fun newInstance() = PredictFragment()
    }

    private lateinit var viewModel: PredictViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_predict, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PredictViewModel::class.java)
        // TODO: Use the ViewModel
    }

}