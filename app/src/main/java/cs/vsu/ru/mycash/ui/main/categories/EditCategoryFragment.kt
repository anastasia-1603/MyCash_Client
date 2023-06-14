package cs.vsu.ru.mycash.ui.main.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import cs.vsu.ru.mycash.R
import cs.vsu.ru.mycash.api.ApiClient
import cs.vsu.ru.mycash.api.ApiService
import cs.vsu.ru.mycash.data.Category
import cs.vsu.ru.mycash.data.CategoryType
import cs.vsu.ru.mycash.databinding.FragmentEditCategoryBinding
import cs.vsu.ru.mycash.utils.AppPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import top.defaults.colorpicker.ColorPickerPopup

class EditCategoryFragment : Fragment() {

    private var _binding: FragmentEditCategoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var apiService: ApiService
    private lateinit var appPrefs: AppPreferences
    private val editCategoryViewModel: EditCategoryViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        appPrefs = activity?.let { AppPreferences(it) }!!
        _binding = FragmentEditCategoryBinding.inflate(inflater, container, false)

        val menu = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).menu
        menu.findItem(R.id.categoriesFragment).isChecked = true

        val category = editCategoryViewModel.category.value


        if (category != null) {
            if (category.type == CategoryType.INCOME)
            {
                binding.limit.isVisible = false
                binding.limitText.isVisible = false
            }
            binding.categName.text = category.name.toString()
            binding.colorBtn.setColorFilter(category.color)

            if (category.isLimited) {
                binding.limit.setText(category.spendingLimit.toString())
            }
        }

        binding.colorBtn.setOnClickListener {
            if (category != null) {
                ColorPickerPopup
                    .Builder(context)
                    .initialColor(category.color)
                    .enableBrightness(true)
                    .okTitle("Выбрать")
                    .cancelTitle("Отменить")
                    .showIndicator(true)
                    .showValue(true)
                    .build()
                    .show(it, object : ColorPickerPopup.ColorPickerObserver() {
                        override fun onColorPicked(color: Int) {
                            category.color = color
                            editCategoryViewModel.setCategory(category)
                            binding.colorBtn.setColorFilter(color)
                        }

                    })
            }
        }

        binding.saveButton.setOnClickListener {
            val categoryName = binding.categName
            if (categoryName.text.trim().isEmpty()) {
                categoryName.error = "Введите название счета"
            } else {
                if (category != null) {
                    val limit = binding.limit.text.toString()
                    if (limit.trim().isNotEmpty()) {
                        if (limit.toDouble() < 0) {
                            binding.limit.error = "Введите положительное значение"
                        }
                        else {
                            category.spendingLimit = limit.toDouble()
                            category.isLimited = true
                        }
                    }
                    editCategoryViewModel.setCategory(category)
                    updateCategory(category)
                }

            }
        }

        binding.deleteButton.setOnClickListener {
            findNavController().navigateUp()
        }
        return binding.root
    }
    private fun updateCategory(category: Category) {
        apiService = ApiClient.getClient(appPrefs.token.toString())
        apiService.updateCategory(category).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                findNavController().navigateUp()
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}