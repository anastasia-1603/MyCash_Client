package cs.vsu.ru.mycash.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import cs.vsu.ru.mycash.R
import cs.vsu.ru.mycash.adapter.OperationAdapter
import cs.vsu.ru.mycash.databinding.ActivityMainScreenBinding
import cs.vsu.ru.mycash.service.OperationService
import cs.vsu.ru.mycash.ui.main.accounts.AccountsFragment
import cs.vsu.ru.mycash.ui.main.categories.CategoriesFragment
import cs.vsu.ru.mycash.ui.main.diagrams.DiagramsFragment
import cs.vsu.ru.mycash.ui.main.home.HomeFragment
import cs.vsu.ru.mycash.ui.main.profile.ProfileFragment

class MainScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainScreenBinding
    private lateinit var adapter: OperationAdapter
    private lateinit var operationService: OperationService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.accountName.text = intent.getStringExtra("accountName")
        binding.balance.text = intent.getStringExtra("balance")

        val manager = LinearLayoutManager(this)
        adapter = OperationAdapter()
        operationService = OperationService()
        adapter.data = operationService.operations
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.adapter = adapter


        binding.navView.selectedItemId = R.id.nav_host_fragment_activity_navigation
        binding.navView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.navigation_diagrams -> {
                    openFragment(DiagramsFragment())
                }
                R.id.navigation_accounts -> {
                    openFragment(AccountsFragment())

                }
                R.id.navigation_categories -> {
                    openFragment(CategoriesFragment())
                }
                R.id.navigation_profile -> {
                    openFragment(ProfileFragment())
                }
                R.id.navigation_home->{
                    openFragment(HomeFragment())
                }
            }
            true
        }
//
//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_activity_navigation) as NavHostFragment
//        val navController = navHostFragment.navController
//
//
//        val navView: BottomNavigationView = binding.navView
//
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        val appBarConfiguration = AppBarConfiguration(
//            setOf(
//                R.id.homeFragment, R.id.accountsFragment, R.id.categoriesFragment,
//                R.id.diagramsFragment, R.id.profileFragment
//            )
//        )
//        navView.setupWithNavController(navController)

    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_view, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}