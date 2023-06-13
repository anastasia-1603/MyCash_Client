package cs.vsu.ru.mycash.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import cs.vsu.ru.mycash.R
import cs.vsu.ru.mycash.databinding.ActivityMainScreenBinding
import cs.vsu.ru.mycash.ui.main.operation.AddOperationFragment
import cs.vsu.ru.mycash.ui.onboarding.WelcomeActivity
import cs.vsu.ru.mycash.utils.AppPreferences

class MainScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainScreenBinding
    private lateinit var navController: NavController
    private lateinit var appPrefs: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        appPrefs = AppPreferences(this)
        if (appPrefs.token == "") {
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        }

        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        binding.navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.profileFragment) {
                if (!appPrefs.isAuth) {
                    if (appPrefs.token == "") {
                        startActivity(Intent(this, WelcomeActivity::class.java))
                        finish()
                    }
                    navController.navigate(R.id.profileUnauthFragment)
                }
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navController.navigateUp()
            }
        })

    }
}