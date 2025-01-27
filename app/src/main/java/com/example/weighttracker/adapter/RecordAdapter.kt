package com.example.weighttracker.adapter
import android.view.LayoutInflater
import java.text.SimpleDateFormat
import java.util.Locale
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weighttracker.data.WeightRecord
import com.example.weighttracker.databinding.ItemRecordBinding

class RecordAdapter(private var records: List<WeightRecord>) : RecyclerView.Adapter<RecordAdapter.RecordViewHolder>() {

    companion object {
        private const val INPUT_DATE_FORMAT = "dd-MM-yyyy HH:mm:ss"
        private const val OUTPUT_DATE_FORMAT = "dd-MM-yyyy HH:mm"
    }

    fun updateRecords(newRecords: List<WeightRecord>) {
        records = newRecords
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val binding = ItemRecordBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        val record = records[position]
        val inputFormat = SimpleDateFormat(INPUT_DATE_FORMAT, Locale.getDefault())
        val outputFormat = SimpleDateFormat(OUTPUT_DATE_FORMAT, Locale.getDefault())

        with(holder.binding) {
            tvDateOutput.text = record.date?.let { date ->
                inputFormat.parse(date)?.let { parsedDate ->
                    outputFormat.format(parsedDate)
                }
            } ?: record.date

            tvWeightOutput.text = "${record.weight} kg"
        }
    }

    override fun getItemCount(): Int = records.size
    class RecordViewHolder(val binding: ItemRecordBinding) : RecyclerView.ViewHolder(binding.root)
}