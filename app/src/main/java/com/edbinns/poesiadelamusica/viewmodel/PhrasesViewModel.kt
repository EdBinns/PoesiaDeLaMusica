package com.edbinns.poesiadelamusica.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.edbinns.poesiadelamusica.models.Phrases
import com.edbinns.poesiadelamusica.usecases.GetListPhrasesUseCase
import com.edbinns.poesiadelamusica.usecases.GetPhraseUpdate
import com.edbinns.poesiadelamusica.usecases.SearchPhrase
import com.edbinns.poesiadelamusica.usecases.ToLiKE
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PhrasesViewModel @Inject constructor(
    private val toLikeUseCase: ToLiKE,
    private val searchPhaseUseCase: SearchPhrase,
    private val getListPhrasesUseCase: GetListPhrasesUseCase,
    private val getPhraseUpdate: GetPhraseUpdate
) : ViewModel() {

    private var phrasesListMutable = MutableLiveData<List<Phrases>>()


    fun setCategory( category: String) {
        phrasesListMutable = getListPhrasesUseCase.invoke(category)
    }

    fun getListUpdate(): LiveData<Phrases> = getPhraseUpdate.invoke()


    fun search(artits: String, category: String) {
        phrasesListMutable = searchPhaseUseCase.invoke(artits, category)
    }

    fun getListPhases(): LiveData<List<Phrases>> = phrasesListMutable


    fun toLike(phrases: Phrases) {
        phrases.likes += 1
        toLikeUseCase?.invoke(phrases)
    }



}