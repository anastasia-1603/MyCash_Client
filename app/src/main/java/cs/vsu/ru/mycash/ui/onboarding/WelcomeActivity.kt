package cs.vsu.ru.mycash.ui.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import cs.vsu.ru.mycash.R

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        val button: Button = findViewById(R.id.button)
        button.setOnClickListener {
            val intent = Intent(this@WelcomeActivity, StartActivity::class.java)
            startActivity(intent)
        }
    }
}