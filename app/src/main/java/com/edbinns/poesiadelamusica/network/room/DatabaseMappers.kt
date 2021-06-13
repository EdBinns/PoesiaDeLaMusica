package com.edbinns.poesiadelamusica.network.room

import com.edbinns.poesiadelamusica.models.Favorites
import com.edbinns.poesiadelamusica.models.Phrases

fun List<Favorites>.toPhrasesList() = map(Favorites::toPhrases)

fun Phrases.toFavorites() = Favorites(
        0,
        uid,
        artist,
        category,
        likes,
        phrase
)

fun Favorites.toPhrases() = Phrases(
        uid,
        artist,
        category,
        likes,
        phrase
)