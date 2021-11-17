package com.edbinns.poesiadelamusica.services.room

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
}