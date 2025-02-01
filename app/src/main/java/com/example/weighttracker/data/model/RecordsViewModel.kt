package com.example.weighttracker.data.model
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.weighttracker.MyApp
import com.example.weighttracker.data.WeightRecord
import kotlinx.coroutines.launch

class RecordsViewModel(application: Application) : AndroidViewModel(application) {
    private val recordsDao = MyApp.database.weightRecordDao()
    val allRecords: LiveData<List<WeightRecord>> = recordsDao.getAllRecords()
    fun removeRecord(record: WeightRecord) {
        viewModelScope.launch {
            recordsDao.deleteRecord(record)
        }
    }
}

