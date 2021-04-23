package com.edbinns.poesiadelamusica.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.edbinns.poesiadelamusica.models.Phrases
import com.edbinns.poesiadelamusica.network.firebase.PHRASES_COLLECTION_NAME
import com.edbinns.poesiadelamusica.usecases.GetListPhrasesUseCase
import com.edbinns.poesiadelamusica.usecases.GetPhraseUpdate
import com.edbinns.poesiadelamusica.usecases.ToLiKE
import com.edbinns.poesiadelamusica.view.adapters.PhrasesAdapter

class PhrasesViewModel : ViewModel(){

    private var phrasesListMutable = MutableLiveData<List<Phrases>>()
    private var toLikeUseCase: ToLiKE? = null
    fun getUseCase(getListPhrasesUseCase: GetListPhrasesUseCase, category:String) {
        phrasesListMutable=  getListPhrasesUseCase.invoke(category)
    }

    fun getListPhases(): LiveData<List<Phrases>> = phrasesListMutable

    fun getUseCase(toLiKE: ToLiKE) {
        toLikeUseCase = toLiKE
    }
    fun toLike(phrases: Phrases){
        toLikeUseCase?.invoke(phrases)
    }

    fun getUseCase(getPhraseUpdate: GetPhraseUpdate){
        getPhraseUpdate.invoke()
    }
}