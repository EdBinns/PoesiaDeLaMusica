package com.edbinns.poesiadelamusica.services.firebase

import android.content.ContentValues.TAG
import android.util.Log
import com.edbinns.poesiadelamusica.models.Phrases
import com.edbinns.poesiadelamusica.view.Utils.descapitalizeAllWords
import com.google.firebase.firestore.FirebaseFirestore

const val PHRASES_COLLECTION_NAME = "frases"

class FirestoreService(private val firebaseFirestore: FirebaseFirestore) {
// Link para la paginacion
//    https://firebase.google.com/docs/firestore/query-data/query-cursors
    fun setDocument(data: Any, collectionName: String, id: String, callback: Callback<Void>) {
        firebaseFirestore.collection(collectionName).document(id).set(data)
                .addOnSuccessListener { callback.onSuccess(null) }
                .addOnFailureListener { exception -> callback.onFailed(exception) }
    }

    fun getPhrases(category: String, callback: Callback<List<Phrases>>) {
        firebaseFirestore.collection(PHRASES_COLLECTION_NAME)
                .whereEqualTo("category", category)
                .get()
                .addOnSuccessListener { documents ->
                    val phrasesList = mutableListOf<Phrases>()
                    for (document in documents) {
                        val phrase = Phrases(document.id, document["artist"] as String, document["category"] as String, document["likes"] as Long, document["phrase"] as String)
                        phrasesList.add(phrase)
                    }
                    callback.onSuccess(phrasesList)
                }
                .addOnFailureListener { exception -> callback.onFailed(exception) }
    }

    fun searchPhrase(artist: String, category: String, callback: Callback<List<Phrases>>) {
        firebaseFirestore.collection(PHRASES_COLLECTION_NAME)
                .whereEqualTo("category", category)
                .get()
                .addOnSuccessListener { documents ->
                    val phrasesList = mutableListOf<Phrases>()
                    for (document in documents) {
                        val artistDoc = document["artist"] as String
                        val str = artistDoc.descapitalizeAllWords()
                        if(str.contains(artist)){
                            val phrase = Phrases(document.id,artistDoc , document["category"] as String, document["likes"] as Long, document["phrase"] as String)
                            phrasesList.add(phrase)
                        }
                    }
                    callback.onSuccess(phrasesList)
                }
                .addOnFailureListener { exception -> callback.onFailed(exception) }
    }
    fun updateLike(phrase: Phrases, callback: Callback<Void>) {
        val docRef = firebaseFirestore.collection(PHRASES_COLLECTION_NAME).document(phrase.uid)
        docRef.update("likes", phrase.likes)
                .addOnFailureListener { exception -> callback.onFailed(exception) }
                .addOnSuccessListener {
                    callback.onSuccess(null)}
    }


    fun listenUpdates(phrase: Phrases, listener: RealtimeDataListener<Phrases>) {
        val docRef = firebaseFirestore.collection(PHRASES_COLLECTION_NAME).document(phrase.uid)

        docRef.addSnapshotListener { value, error ->
            if (error != null) {
                Log.w(TAG, "Listen failed.", error)
                listener.onError(error)
                return@addSnapshotListener
            }

            if (value != null && value.exists()) {
                Log.d(TAG, "Current data: ${value.data}")
                val phrase = Phrases(value.id, value["artist"] as String, value["category"] as String, value["likes"] as Long, value["phrase"] as String)
                listener.onDataChange(phrase)
            } else {
                Log.d(TAG, "Current data: null")
            }
        }
    }

    fun listenUpdates(listener: RealtimeDataListener<Phrases>) {
        val collectionName = firebaseFirestore.collection(PHRASES_COLLECTION_NAME)

        collectionName.addSnapshotListener { value, error ->
            if (error != null) {
                Log.w(TAG, "Listen failed.", error)
                listener.onError(error)
                return@addSnapshotListener
            }

            if (value != null && !value.isEmpty) {
//                val phrasesList = mutableListOf<Phrases>()
                for (documentChange in value.documentChanges) {
                    val document = documentChange.document
                    val phrase = Phrases(document.id, document["artist"] as String, document["category"] as String, document["likes"] as Long, document["phrase"] as String)
                    listener.onDataChange(phrase)
                }

            } else {
                Log.d(TAG, "Current data: null")
            }
        }
    }
}