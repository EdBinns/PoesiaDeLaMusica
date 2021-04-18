package com.edbinns.poesiadelamusica.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.edbinns.poesiadelamusica.models.Phrases
import com.edbinns.poesiadelamusica.usecases.GetListPhrasesUseCase
import com.edbinns.poesiadelamusica.view.adapters.PhrasesAdapter

class PhrasesViewModel : ViewModel(){

    private var phrasesListMutable = MutableLiveData<List<Phrases>>()


    fun getUseCase(getListPhrasesUseCase: GetListPhrasesUseCase, category:String) {
        phrasesListMutable=  getListPhrasesUseCase.invoke(category)
    }

    fun getListPhases(): LiveData<List<Phrases>> = phrasesListMutable

}