package com.edbinns.poesiadelamusica.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
data class Favorites(
    @PrimaryKey(autoGenerate = true)
    val id:Int,
    val uid: String,
    val artist: String,
    val category: String,
    var likes: Long,
    val phrase: String
)
