package com.edbinns.poesiadelamusica.network.repositorys

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.edbinns.poesiadelamusica.models.Favorites
import com.edbinns.poesiadelamusica.models.Phrases
import com.edbinns.poesiadelamusica.network.room.FavoriteDao

class FavoritesRepository(private val favoriteDao: FavoriteDao) {

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