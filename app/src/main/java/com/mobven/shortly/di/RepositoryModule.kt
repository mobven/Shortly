package com.mobven.shortly.di

import com.mobven.shortly.data.repository.MainRepository
import com.mobven.shortly.data.repository.MainRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindMainRepository(impl: MainRepositoryImpl): MainRepository
}