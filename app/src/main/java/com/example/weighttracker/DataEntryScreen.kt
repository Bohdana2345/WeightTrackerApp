package com.example.weighttracker
import java.text.SimpleDateFormat
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.weighttracker.databinding.ActivityDataEntryScreenBinding
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import android.widget.Toast
import kotlin.math.roundToInt
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class DataEntryScreen : AppCompatActivity() {
    private lateinit var binding: ActivityDataEntryScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataEntryScreenBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        supportActionBar?.hide()
        setContentView(binding.root)

        val numberPicker = binding.numberPicker
        val step = 0.1
        val minValue = 30.0
        val maxValue = 200.0

        val decimalValues = Array(((maxValue - minValue) / step + 1).toInt()) { i ->
            String.format("%.1f", minValue + i * step)
        }

        numberPicker.minValue = 0
        numberPicker.maxValue = decimalValues.size - 1
        numberPicker.displayedValues = decimalValues
        numberPicker.setOnValueChangedListener { _, _, newVal ->
            decimalValues[newVal].toDouble()
        }

        ViewCompat.setOnApplyWindowInsetsListener((binding.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.button.setOnClickListener {
            val weight = decimalValues[numberPicker.value].toDouble()
            val roundedWeight = ((weight * 10).roundToInt() / 10.0)
            val timeZone = TimeZone.getDefault()
            val dateWithoutTimeZone = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
            dateWithoutTimeZone.timeZone = timeZone
            val date = dateWithoutTimeZone.format(Date())
            val newRecord = WeightRecord(weight = roundedWeight, date = date)
            lifecycleScope.launch {
                MyApp.database.weightRecordDao().insertWeightRecord(newRecord)
                Toast.makeText(this@DataEntryScreen, "Saved successfully:)", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}