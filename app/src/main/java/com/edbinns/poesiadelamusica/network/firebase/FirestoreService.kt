package com.edbinns.poesiadelamusica.network.firebase

import com.edbinns.poesiadelamusica.models.Phrases
import com.google.firebase.firestore.FirebaseFirestore

const val PHRASES_COLLECTION_NAME = "frases"

class FirestoreService (private val firebaseFirestore: FirebaseFirestore){

    fun setDocument(data: Any, collectionName: String, id: String, callback: Callback<Void>) {
        firebaseFirestore.collection(collectionName).document(id).set(data)
            .addOnSuccessListener { callback.onSuccess(null) }
            .addOnFailureListener { exception -> callback.onFailed(exception) }
    }

    fun getPhrases(category: String, callback: Callback<List<Phrases>>){
        firebaseFirestore.collection(PHRASES_COLLECTION_NAME)
            .whereEqualTo("category",category)
            .get()
            .addOnSuccessListener { documents ->
                val phrasesList = mutableListOf<Phrases>()
                for (document in documents) {
                    println("Frases ${document["likes"] is Long}")
                    val phrase = Phrases(document.id, document["artist"] as String, document["category"] as String,document["likes"] as Long, document["phrase"] as String)
                    phrasesList.add(phrase)
                }
                callback.onSuccess(phrasesList)
            }
            .addOnFailureListener { exception -> callback.onFailed(exception) }
    }
}