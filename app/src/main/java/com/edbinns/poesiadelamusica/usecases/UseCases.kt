package com.edbinns.poesiadelamusica.usecases

import androidx.lifecycle.MutableLiveData
import com.edbinns.poesiadelamusica.models.Phrases
import com.edbinns.poesiadelamusica.network.repositorys.PhrasesRespository

//
//class GetEpisodeFromCharacterUseCase(
//        private val episodeRepository: EpisodeRepository
//) {
//
//    fun invoke(episodeUrlList: List<String>): Single<List<Episode>> =
//            episodeRepository.getEpisodeFromCharacter(episodeUrlList)
//}


class GetListPhrasesUseCase(private val phrasesRespository: PhrasesRespository){

    fun invoke(category : String) = phrasesRespository.getPhrasesListLiveData(category)

}