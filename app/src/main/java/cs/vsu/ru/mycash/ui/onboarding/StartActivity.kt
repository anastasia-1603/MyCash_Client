package cs.vsu.ru.mycash.ui.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import cs.vsu.ru.mycash.R
import cs.vsu.ru.mycash.ui.main.MainScreenActivity

class StartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        val button : Button = findViewById(R.id.btnContinue)
        button.setOnClickListener {
            val intent = Intent(this@StartActivity, MainScreenActivity::class.java)
            startActivity(intent)
        }

    }
}