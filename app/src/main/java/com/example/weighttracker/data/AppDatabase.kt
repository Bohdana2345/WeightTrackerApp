package com.example.weighttracker.data
import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WeightRecord::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weightRecordDao(): WeightRecordDao
}