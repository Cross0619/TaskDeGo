package com.example.taskdego.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.taskdego.data.dao.tTaskDao
import com.example.taskdego.data.dao.tTrainerProfileDao
import com.example.taskdego.data.entity.tTaskEntity
import com.example.taskdego.data.entity.tTrainerProfileEntity

@Database(entities = [tTaskEntity::class, tTrainerProfileEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun tTaskDao(): tTaskDao
    abstract fun tTrainerProfileDao(): tTrainerProfileDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "task_de_go_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}