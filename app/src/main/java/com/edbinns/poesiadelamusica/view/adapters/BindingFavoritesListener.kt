package com.edbinns.poesiadelamusica.view.adapters

import com.edbinns.poesiadelamusica.databinding.ItemFavoriteBinding
import com.edbinns.poesiadelamusica.models.Phrases

interface BindingFavoritesListener {

    fun bindingListener(binding: ItemFavoriteBinding, data: Phrases)
}