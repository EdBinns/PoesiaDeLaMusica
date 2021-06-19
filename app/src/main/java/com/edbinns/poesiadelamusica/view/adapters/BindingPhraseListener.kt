package com.edbinns.poesiadelamusica.view.adapters

import com.edbinns.poesiadelamusica.databinding.ItemFavoriteBinding
import com.edbinns.poesiadelamusica.databinding.ItemPhrasesBinding
import com.edbinns.poesiadelamusica.models.Phrases

interface BindingPhraseListener {

    fun setAnimInButtons(bindingItem: ItemPhrasesBinding, data : Phrases)
}