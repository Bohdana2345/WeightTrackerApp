package com.example.weighttracker
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData

class RecordsViewModel(application: Application) : AndroidViewModel(application) {
    private val recordsDao = MyApp.database.weightRecordDao()
    val allRecords: LiveData<List<WeightRecord>> = recordsDao.getAllRecords()
}

