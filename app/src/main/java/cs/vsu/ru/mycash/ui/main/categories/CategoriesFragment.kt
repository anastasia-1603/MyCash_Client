package cs.vsu.ru.mycash.ui.main.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import cs.vsu.ru.mycash.api.ApiAuthClient
import cs.vsu.ru.mycash.api.ApiService
import cs.vsu.ru.mycash.data.Category
import cs.vsu.ru.mycash.data.CategoryType
import cs.vsu.ru.mycash.databinding.FragmentCategoriesBinding
import cs.vsu.ru.mycash.utils.AppPreferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoriesFragment : Fragment() {
    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding!!
    private val categoriesViewModel: CategoriesViewModel by activityViewModels()
    private lateinit var apiService: ApiService
    private lateinit var appPrefs: AppPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val categoriesViewModel =
//            ViewModelProvider(this).get(CategoriesViewModel::class.java)

        appPrefs = activity?.let { AppPreferences(it) }!!

        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val viewPager = binding.viewPager
        viewPager.adapter = CategoriesPagerAdapter(this)
        val tabLayoutMediator =
            TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                when (position) {
                    0 -> tab.text = "Доходы"
                    1 -> tab.text = "Расходы"
                }
            }
        tabLayoutMediator.attach()
        loadCategories()

        return root
    }

    fun loadCategories()
    {
        val token = appPrefs.token.toString()
        Log.e("token home prefs", appPrefs.token.toString())
        val apiService = ApiAuthClient.getClient(token).create(ApiService::class.java)
//        apiService = ApiAuthClient.getClient(requireActivity()).create(ApiService::class.java)
        apiService.getCategories().enqueue(object : Callback<List<Category>> {
            override fun onResponse(
                call: Call<List<Category>>,
                response: Response<List<Category>>
            ) {
                response.body()?.let { categoriesViewModel.setCategoryList(it) }
                categoriesViewModel.categoryList.value?.let { categories ->
                    categoriesViewModel.setExpenseList(
                        categories.filter { it.type == CategoryType.EXPENSE })
                }
                categoriesViewModel.categoryList.value?.let { categories ->
                    categoriesViewModel.setIncomeList(
                        categories.filter { it.type == CategoryType.INCOME })
                }
            }

            override fun onFailure(call: Call<List<Category>>, t: Throwable) {
                t.message?.let { Log.e("t", it) }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class CategoriesPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        override fun getItemCount(): Int {
            return 2
        }

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> CategoriesIncomeFragment()
                1 -> CategoriesExpensesFragment()
                else -> throw IllegalArgumentException("Invalid position")
            }
        }
    }

}