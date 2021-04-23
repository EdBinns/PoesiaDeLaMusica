package com.edbinns.poesiadelamusica.network.repositorys

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.edbinns.poesiadelamusica.models.Phrases
import com.edbinns.poesiadelamusica.network.firebase.Callback
import com.edbinns.poesiadelamusica.network.firebase.FirestoreService
import com.edbinns.poesiadelamusica.network.firebase.RealtimeDataListener
import com.google.android.material.tabs.TabLayout
import java.lang.Exception
import java.lang.ref.PhantomReference

class PhrasesRespository( private val firestoreService: FirestoreService) {

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
                phrasesListLiveData.postValue(result)
                result?.forEach { p ->
                    println("For en la lista")
                    listenUpdate(p)
                }
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
    private fun listenUpdate(phrases: Phrases){
        firestoreService.listenUpdates(phrases,object :RealtimeDataListener<Phrases>{
            override fun onDataChange(updatedData: Phrases) {
                println("Dato nuevo $updatedData")
                phrasesUpdate.postValue(updatedData)
            }

            override fun onError(exception: Exception) {
                println("Error al actualizar $exception")
            }

        })
    }
}