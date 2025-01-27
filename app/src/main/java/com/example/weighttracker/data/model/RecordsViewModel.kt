package com.example.weighttracker.data.model
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.weighttracker.MyApp
import com.example.weighttracker.data.WeightRecord

class RecordsViewModel(application: Application) : AndroidViewModel(application) {
    private val recordsDao = MyApp.database.weightRecordDao()
    val allRecords: LiveData<List<WeightRecord>> = recordsDao.getAllRecords()
}

