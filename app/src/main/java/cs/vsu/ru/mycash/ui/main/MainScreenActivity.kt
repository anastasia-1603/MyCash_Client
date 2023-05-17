package cs.vsu.ru.mycash.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
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
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        binding.navView.setupWithNavController(navController)

//        binding.navView.setOnItemSelectedListener {
//            when(it.itemId) {
//                R.id.navigation_diagrams -> {
//                    openFragment(DiagramsFragment())
//                }
//                R.id.navigation_accounts -> {
//                    openFragment(AccountsFragment())
//                }
//                R.id.navigation_categories -> {
//                    openFragment(CategoriesFragment())
//                }
//                R.id.navigation_profile -> {
//                    openFragment(ProfileFragment())
//                }
//                R.id.navigation_home->{
//                    openFragment(HomeFragment())
//                }
//            }
//            true
//        }
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
        transaction.replace(R.id.nav_host_fragment, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}