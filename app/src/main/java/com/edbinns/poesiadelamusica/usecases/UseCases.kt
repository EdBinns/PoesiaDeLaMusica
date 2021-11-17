package com.edbinns.poesiadelamusica.usecases

import com.edbinns.poesiadelamusica.models.Phrases
import com.edbinns.poesiadelamusica.services.repositorys.PhrasesRespository
import javax.inject.Inject


class GetListPhrasesUseCase @Inject constructor(private val phrasesRespository: PhrasesRespository) {

    fun invoke(category: String) = phrasesRespository.getPhrasesListLiveData(category)

}

class ToLiKE  @Inject constructor(private val phrasesRespository: PhrasesRespository) {
    fun invoke(phrases: Phrases) {
        phrasesRespository.toLike(phrases)
    }
}

class GetPhraseUpdate  @Inject constructor(private val phrasesRespository: PhrasesRespository) {
    fun invoke() = phrasesRespository.getPhraseUpdate()
}

class ListenUpdate  @Inject constructor(private val phrasesRespository: PhrasesRespository){
    fun invoke() = phrasesRespository.listenCollectionUpdate()
}

class SearchPhrase @Inject constructor(private val phrasesRespository: PhrasesRespository){
    fun invoke(artist: String,category: String) = phrasesRespository.searchPhrase(artist,category)
}
