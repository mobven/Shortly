package com.example.samplerunproject.di

import com.example.samplerunproject.api.ApiService
import com.example.samplerunproject.repository.MainRepository
import com.example.samplerunproject.room.LinkDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideMainRepository(
        apiService: ApiService,
        linkDao: LinkDao
    ) = MainRepository(apiService, linkDao)
}