package com.edbinns.poesiadelamusica.services.repositorys

import androidx.lifecycle.MutableLiveData
import com.edbinns.poesiadelamusica.models.Phrases
import com.edbinns.poesiadelamusica.services.firebase.Callback
import com.edbinns.poesiadelamusica.services.firebase.FirestoreService
import com.edbinns.poesiadelamusica.services.firebase.RealtimeDataListener
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PhrasesRespository @Inject constructor( private val firestoreService: FirestoreService) {

    private val phrasesListLiveData: MutableLiveData<List<Phrases>> = MutableLiveData()
    private val phrasesUpdate: MutableLiveData<Phrases> = MutableLiveData()

    fun getPhrasesListLiveData(category: String):MutableLiveData<List<Phrases>>{
        getPhrasesListFromFirebase(category)
        return phrasesListLiveData
    }

    fun getPhraseUpdate() = phrasesUpdate

    private fun getPhrasesListFromFirebase(category: String){
        firestoreService.getPhrases(category, object : Callback<List<Phrases>>{
            override fun onSuccess(result: List<Phrases>?) {
                listenCollectionUpdate()
                phrasesListLiveData.postValue(result)
            }
            override fun onFailed(exception: Exception) {
                println("Errror al obtener las frases ${exception.message}")
            }
        })
    }

    fun toLike(phrases: Phrases){
        firestoreService.updateLike(phrases, object : Callback<Void>{

            override fun onFailed(exception: Exception) {
                println("Error al actualizar")
            }

            override fun onSuccess(result: Void?) {
                println("Like actualizado")
            }

        })
    }

     fun listenCollectionUpdate() {
        firestoreService.listenUpdates(object : RealtimeDataListener<Phrases> {
            override fun onDataChange(updatedData: Phrases) {
                println("frase actualizada $updatedData")
                phrasesUpdate.postValue(updatedData)
            }

            override fun onError(exception: Exception) {
                println("Error al actualizar ${exception.message}")
            }

        })
    }
    fun searchPhrase(artist:String,category: String):MutableLiveData<List<Phrases>>{
        searchPhraseByArtist(artist,category)
        return phrasesListLiveData
    }


    private fun searchPhraseByArtist(artist:String,category: String){
        firestoreService.searchPhrase(artist,category, object :Callback<List<Phrases>>{
            override fun onSuccess(result: List<Phrases>?) {
                phrasesListLiveData.postValue(result)
            }

            override fun onFailed(exception: Exception) {
                println("Errror al buscar las frases ${exception.message}")
            }

        })

    }
}