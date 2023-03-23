package com.campuscoders.ventmind.di

import com.campuscoders.ventmind.repo.*
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

    @Provides
    @Singleton
    fun provideFeedRepository(database: FirebaseFirestore): FeedRepository {
        return FeedRepositoryImp(database)
    }

    @Provides
    @Singleton
    fun provideProfileRepository(database: FirebaseFirestore, auth: FirebaseAuth): ProfileRepository {
        return ProfileRepositoryImp(database,auth)
    }

    @Provides
    @Singleton
    fun provideCreatePostRepository(database: FirebaseFirestore, auth: FirebaseAuth): CreatePostRepository {
        return CreatePostRepositoryImp(database, auth)
    }
}