package com.example.weighttracker.presentation.feature
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weighttracker.databinding.ActivityMainBinding
import android.content.Intent
import android.view.View
import androidx.activity.viewModels
import com.example.weighttracker.adapter.RecordAdapter
import com.example.weighttracker.data.model.RecordsViewModel


class MainActivity : ComponentActivity() {
    private var binding: ActivityMainBinding? = null
    private var recordAdapter: RecordAdapter? = null
    private val recordsViewModel: RecordsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        enableEdgeToEdge()
        recordAdapter = RecordAdapter(emptyList())

        binding?.rvRecordsList?.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = recordAdapter
        }

        recordsViewModel.allRecords.observe(this) { records ->
            recordAdapter?.updateRecords(records)
            if (records.isEmpty()) {
                binding?.rvRecordsList?.visibility = View.GONE
            } else {
                binding?.rvRecordsList?.visibility = View.VISIBLE
            }
        }
        binding?.bAddRecord?.setOnClickListener {
            val intent = Intent(this, DataEntryScreen::class.java)
            startActivity(intent)
        }
    }
}


