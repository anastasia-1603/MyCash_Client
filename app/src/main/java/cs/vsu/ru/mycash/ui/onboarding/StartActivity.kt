package cs.vsu.ru.mycash.ui.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import cs.vsu.ru.mycash.R
import cs.vsu.ru.mycash.databinding.ActivityStartBinding
import cs.vsu.ru.mycash.ui.main.MainScreenActivity

class StartActivity : AppCompatActivity() {

    private lateinit var binding : ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnContinue.setOnClickListener {
            val intent = Intent(this@StartActivity, MainScreenActivity::class.java)
            intent.putExtra("accountName", binding.editTextAccountName.text.toString())
            intent.putExtra("balance", binding.editTextBalance.text.toString())
            startActivity(intent)
        }

    }
}