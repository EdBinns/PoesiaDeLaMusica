package com.edbinns.poesiadelamusica.services.repositorys

import androidx.lifecycle.LiveData
import com.edbinns.poesiadelamusica.models.Favorites
import com.edbinns.poesiadelamusica.services.room.FavoriteDao
import javax.inject.Inject

class FavoritesRepository @Inject constructor(private val favoriteDao: FavoriteDao) {

    val getAll: LiveData<List<Favorites>> = favoriteDao.getAll()


    suspend fun addFavorite(favorite: Favorites) {
        favoriteDao.insert(favorite)
    }

    suspend  fun getById(uid: String):Favorites{
        return  favoriteDao.getByID(uid)
    }

    suspend  fun update(favorites: Favorites){
        favoriteDao.update(favorites)
    }

    suspend fun delete(favorites: Favorites){
        favoriteDao.delete(favorites)
    }
}