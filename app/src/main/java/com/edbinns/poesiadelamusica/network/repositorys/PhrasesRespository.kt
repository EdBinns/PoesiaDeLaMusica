package com.edbinns.poesiadelamusica.network.repositorys

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.edbinns.poesiadelamusica.models.Phrases
import com.edbinns.poesiadelamusica.network.firebase.Callback
import com.edbinns.poesiadelamusica.network.firebase.FirestoreService
import com.google.android.material.tabs.TabLayout
import java.lang.Exception
import java.lang.ref.PhantomReference

class PhrasesRespository( private val firestoreService: FirestoreService) {

    private val phrasesListLiveData: MutableLiveData<List<Phrases>> = MutableLiveData()

    fun getPhrasesListLiveData(category: String):MutableLiveData<List<Phrases>>{
        getPhrasesListFromFirebase(category)
        return phrasesListLiveData
    }

    private fun getPhrasesListFromFirebase(category: String){
        firestoreService.getPhrases(category, object : Callback<List<Phrases>>{
            override fun onSuccess(result: List<Phrases>?) {
                phrasesListLiveData.postValue(result)
            }

            override fun onFailed(exception: Exception) {
                println("Errror al obtener las frases ${exception.message}")
            }
        })
    }
}