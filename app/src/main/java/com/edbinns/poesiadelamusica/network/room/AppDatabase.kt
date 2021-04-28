package com.edbinns.poesiadelamusica.network.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.edbinns.poesiadelamusica.models.PhrasesTable


@Database(entities = [PhrasesTable::class], version =1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun phrases():PhrasesDAO


    companion object {
        private var INSTANCE: AppDatabase? = null
        fun getAppDatabase(context: Context): AppDatabase? {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "room-kotlin-database"
                ).build()
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}