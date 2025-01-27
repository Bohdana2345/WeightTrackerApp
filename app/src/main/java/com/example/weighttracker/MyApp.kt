package com.example.weighttracker
import android.app.Application
import androidx.room.Room
import com.example.weighttracker.data.AppDatabase

class MyApp : Application() {
    companion object {
        lateinit var database: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
            database = Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                "my_database"
            )
                .allowMainThreadQueries()
                .build()
            database.openHelper.writableDatabase
    }
}
