package com.edbinns.poesiadelamusica.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "phrases")
data class PhrasesTable(
    @PrimaryKey()
    val uid : String,
    val artist: String,
    val category: String,
    var likes: Long,
    val phrase: String
    )
