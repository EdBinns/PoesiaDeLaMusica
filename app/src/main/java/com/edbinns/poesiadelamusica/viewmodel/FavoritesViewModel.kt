package com.edbinns.poesiadelamusica.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.edbinns.poesiadelamusica.models.Favorites
import com.edbinns.poesiadelamusica.models.Phrases
import com.edbinns.poesiadelamusica.services.repositorys.FavoritesRepository
import com.edbinns.poesiadelamusica.services.room.FavoritesDB
import com.edbinns.poesiadelamusica.services.room.toFavorites
import com.edbinns.poesiadelamusica.usecases.GetPhraseUpdate
import com.edbinns.poesiadelamusica.usecases.ListenUpdate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FavoritesViewModel(application: Application):AndroidViewModel(application){


    val favoritesList : LiveData<List<Favorites>>
    private val repository: FavoritesRepository
    init {
        val favoriteDao = FavoritesDB.getDB(application).favoritesDao()
        repository = FavoritesRepository(favoriteDao)
        favoritesList = repository.getAll
    }

    fun addFavorite(phrases: Phrases) {
        val favorite = phrases.toFavorites()
        viewModelScope.launch(Dispatchers.IO) {
            val temp: Favorites? = repository.getById(favorite.uid)
            if (temp == null)
                repository.addFavorite(favorite)
        }
    }

    fun getUseCase(getPhraseUpdate: GetPhraseUpdate) = getPhraseUpdate.invoke()


    fun listenUpdate(listen : ListenUpdate){ listen.invoke() }

    fun updateFavorites(phrases: Phrases){
        viewModelScope.launch(Dispatchers.IO) {
            val temp: Favorites?= repository.getById(phrases.uid)
            temp?.let {
                temp.likes = phrases.likes
                repository.update(temp)
            }
        }
    }

    fun deleteFavorite(phrases: Phrases){
        viewModelScope.launch(Dispatchers.IO) {
            val temp: Favorites?= repository.getById(phrases.uid)
            temp?.let {
                repository.delete(temp)
            }
        }
    }
}