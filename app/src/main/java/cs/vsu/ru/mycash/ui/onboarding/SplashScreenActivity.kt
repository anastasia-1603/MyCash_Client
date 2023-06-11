package cs.vsu.ru.mycash.ui.onboarding

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import cs.vsu.ru.mycash.databinding.ActivitySplashScreenBinding
import cs.vsu.ru.mycash.ui.main.MainScreenActivity
import cs.vsu.ru.mycash.utils.AppPreferences

@SuppressLint("CustomSplashScreen")
class SplashScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding
    private lateinit var appPrefs: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        appPrefs = AppPreferences(this)

        if (appPrefs.isFirstTimeLaunch) {
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, WelcomeActivity::class.java)
                startActivity(intent)
                finish()
            }, 3000)
        } else {
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, MainScreenActivity::class.java)
                startActivity(intent)
                finish()
            }, 3000)
        }
    }
}