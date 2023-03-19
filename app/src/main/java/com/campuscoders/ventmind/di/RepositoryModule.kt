package com.campuscoders.ventmind.di

import com.campuscoders.ventmind.repo.AuthRepository
import com.campuscoders.ventmind.repo.AuthRepositoryImp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {

    @Provides
    @Singleton
    fun provideAuthRepository(database: FirebaseFirestore, auth: FirebaseAuth): AuthRepository {
        return AuthRepositoryImp(auth,database)
    }
}