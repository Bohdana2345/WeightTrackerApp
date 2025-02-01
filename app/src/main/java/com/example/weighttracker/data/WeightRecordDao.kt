package com.example.weighttracker.data
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.lifecycle.LiveData
import androidx.room.Delete

@Dao
interface WeightRecordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeightRecord(record: WeightRecord)

    @Query("SELECT * FROM weight_records ORDER BY date DESC")
    fun getAllRecords(): LiveData<List<WeightRecord>>

    @Delete
    fun deleteRecord(record: WeightRecord)
}