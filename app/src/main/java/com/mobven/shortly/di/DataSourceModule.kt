package com.mobven.shortly.di

import com.mobven.shortly.data.ShortlyLocalDataSource
import com.mobven.shortly.data.ShortlyLocalDataSourceImpl
import com.mobven.shortly.data.ShortlyRemoteDataSource
import com.mobven.shortly.data.ShortlyRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Binds
    abstract fun bindRemoteDataSource(impl: ShortlyRemoteDataSourceImpl): ShortlyRemoteDataSource

    @Binds
    abstract fun bindLocalDataSource(impl: ShortlyLocalDataSourceImpl): ShortlyLocalDataSource
}