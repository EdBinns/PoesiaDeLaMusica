package com.edbinns.poesiadelamusica.network.room

import android.content.ContentResolver
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.edbinns.poesiadelamusica.models.Favorites

@Database(
    entities = [Favorites::class],
    version = 1,
    exportSchema = false
)
abstract class FavoritesDB: RoomDatabase(){
    abstract fun favoritesDao(): FavoriteDao
    companion object{
        @Volatile
        private var INSTANCE: FavoritesDB? = null;

        fun getDB(context : Context): FavoritesDB{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }

            synchronized(this){
                val instance = Room.databaseBuilder(
                    context,
                    FavoritesDB::class.java,
                    "favoriteDB"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}