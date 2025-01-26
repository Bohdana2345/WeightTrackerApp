package com.example.weighttracker
import android.view.LayoutInflater
import java.text.SimpleDateFormat
import java.util.Locale
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.weighttracker.databinding.ItemRecordBinding

class RecordAdapter(private var records: List<WeightRecord>) : RecyclerView.Adapter<RecordAdapter.RecordViewHolder>() {
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
        val inputFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())

            val parsedDate = inputFormat.parse(record.date)
            parsedDate?.let {
                val formattedDateWithoutSeconds = outputFormat.format(it)
                holder.binding.date.text = formattedDateWithoutSeconds} ?: run {
                holder.binding.date.text = record.date
            }
            holder.binding.weight.text = "${record.weight} kg"
    }

    override fun getItemCount(): Int = records.size
    class RecordViewHolder(val binding: ItemRecordBinding) : RecyclerView.ViewHolder(binding.root)
}