package cs.vsu.ru.mycash.ui.onboarding

import android.content.Intent
import android.icu.util.Output
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup
import android.view.LayoutInflater
import android.widget.Button
import com.github.javafaker.Bool
import cs.vsu.ru.mycash.R
import cs.vsu.ru.mycash.databinding.ActivityStartBinding
import cs.vsu.ru.mycash.ui.main.MainScreenActivity

class StartActivity : AppCompatActivity() {

    private lateinit var binding : ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val balance = binding.editTextBalance
        val accountName = binding.editTextAccountName

        binding.btnContinue.setOnClickListener {
            var valid :Boolean = true
            if (balance.text.trim().isEmpty())
            {
                balance.error = "Введите остаток"
                valid = false
            }
            if (accountName.text.trim().isEmpty())
            {
                accountName.error = "Введите название"
                valid = false
            }
            if (valid)
            {
                val intent = Intent(this@StartActivity, MainScreenActivity::class.java)
                intent.putExtra("accountName", binding.editTextAccountName.text.toString())
                intent.putExtra("balance", binding.editTextBalance.text.toString())
                startActivity(intent)
            }

        }

    }

}