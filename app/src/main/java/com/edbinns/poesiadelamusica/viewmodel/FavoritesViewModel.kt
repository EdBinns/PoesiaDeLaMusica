package com.edbinns.poesiadelamusica.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.edbinns.poesiadelamusica.models.Favorites
import com.edbinns.poesiadelamusica.models.Phrases
import com.edbinns.poesiadelamusica.services.repositorys.FavoritesRepository
import com.edbinns.poesiadelamusica.services.room.FavoritesDB
import com.edbinns.poesiadelamusica.services.room.toFavorites
import com.edbinns.poesiadelamusica.usecases.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class FavoritesViewModel@Inject constructor(
    private val listen: ListenUpdate,
    private val getPhraseUpdate: GetPhraseUpdate,
    private val repository: FavoritesRepository,
    application: Application):AndroidViewModel(application){

    val favoritesList : LiveData<List<Favorites>> = repository.getAll

    fun addFavorite(phrases: Phrases) {
        val favorite = phrases.toFavorites()
        viewModelScope.launch(Dispatchers.IO) {
            val temp: Favorites? = repository.getById(favorite.uid)
            if (temp == null)
                repository.addFavorite(favorite)
        }
    }

    fun getListUpdate() = getPhraseUpdate.invoke()


    fun listenUpdate(){ listen.invoke() }

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