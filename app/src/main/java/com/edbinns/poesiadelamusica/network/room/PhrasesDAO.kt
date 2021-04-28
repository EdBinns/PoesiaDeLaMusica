package com.edbinns.poesiadelamusica.network.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.edbinns.poesiadelamusica.models.PhrasesTable

@Dao
interface PhrasesDAO {
    @Insert
    fun insert(phrasesTable: PhrasesTable)

    @Update
    fun update(phrasesTable: PhrasesTable)

    @Update
    fun delete(phrasesTable: PhrasesTable)

    @Query("SELECT * FROM PHRASES")
    fun getAllPhrases():List<PhrasesTable>

    @Query("SELECT * FROM PHRASES WHERE uid==:id")
    fun getPhraseWithId(id:Int):PhrasesTable
}