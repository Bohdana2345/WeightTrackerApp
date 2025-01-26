package com.example.weighttracker
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weighttracker.databinding.ActivityMainBinding
import android.content.Intent
import android.view.View

class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var recordsViewModel: RecordsViewModel
    private lateinit var recordAdapter: RecordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        recordAdapter = RecordAdapter(emptyList())

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = recordAdapter
        }

        recordsViewModel = ViewModelProvider(this)[RecordsViewModel::class.java]
        recordsViewModel.allRecords.observe(this) { records ->
            recordAdapter.updateRecords(records)
            if (records.isEmpty()) {
                binding.recyclerView.visibility = View.GONE
            } else {
                binding.recyclerView.visibility = View.VISIBLE
            }
        }
        val mainButton = binding.mainButton
        mainButton.setOnClickListener {
            val intent = Intent(this, DataEntryScreen::class.java)
            startActivity(intent)
        }
    }
}


