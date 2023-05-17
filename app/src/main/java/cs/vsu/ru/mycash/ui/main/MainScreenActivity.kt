package cs.vsu.ru.mycash.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cs.vsu.ru.mycash.R
import cs.vsu.ru.mycash.adapter.OperationAdapter
import cs.vsu.ru.mycash.databinding.ActivityMainScreenBinding
import cs.vsu.ru.mycash.service.OperationService
import cs.vsu.ru.mycash.service.OperationServiceApp

class MainScreenActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainScreenBinding
    private lateinit var adapter: OperationAdapter
    private lateinit var operationService: OperationService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val manager = LinearLayoutManager(this)
        adapter = OperationAdapter()
        operationService = OperationService()
        adapter.data = operationService.operations
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.adapter = adapter

    }
}