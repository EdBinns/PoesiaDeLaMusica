package com.edbinns.poesiadelamusica.usecases

import androidx.lifecycle.MutableLiveData
import com.edbinns.poesiadelamusica.models.Phrases
import com.edbinns.poesiadelamusica.network.repositorys.PhrasesRespository



class GetListPhrasesUseCase(private val phrasesRespository: PhrasesRespository) {

    fun invoke(category: String) = phrasesRespository.getPhrasesListLiveData(category)

}

class ToLiKE(private val phrasesRespository: PhrasesRespository) {
    fun invoke(phrases: Phrases) {
        phrasesRespository.toLike(phrases)
    }
}

class GetPhraseUpdate(private val phrasesRespository: PhrasesRespository) {
    fun invoke() = phrasesRespository.getPhraseUpdate()
}