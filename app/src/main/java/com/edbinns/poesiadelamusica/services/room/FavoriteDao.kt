package com.edbinns.poesiadelamusica.services.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.edbinns.poesiadelamusica.models.Favorites

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorite")
    fun getAll(): LiveData<List<Favorites>>

    @Query("SELECT * FROM favorite WHERE uid=:uid")
    suspend fun getByID(uid: String): Favorites

    @Update
    suspend fun update(favorites: Favorites)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(favorites: Favorites)

    @Delete
    suspend fun delete(favorites: Favorites)
}