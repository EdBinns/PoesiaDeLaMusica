package com.edbinns.poesiadelamusica.di

import android.content.Context
import androidx.room.Room
import com.edbinns.poesiadelamusica.services.room.FavoritesDB
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    //proveo room
    @Singleton
    @Provides
    fun provideRoom(@ApplicationContext app: Context) = Room.databaseBuilder(
        app,
        FavoritesDB::class.java,
        "favoriteDB"
    ).build()

    @Singleton
    @Provides
    fun provideDao(db: FavoritesDB) = db.favoritesDao()

    @Singleton
    @Provides
    fun provideFirebaseFirestore() = FirebaseFirestore.getInstance()
}