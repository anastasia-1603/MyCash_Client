package cs.vsu.ru.mycash.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import cs.vsu.ru.mycash.R
import cs.vsu.ru.mycash.databinding.ActivityMainScreenBinding
class MainScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainScreenBinding
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = Navigation.findNavController(this, R.id.nav_host_fragment)
        binding.navView.setupWithNavController(navController)


    }

}