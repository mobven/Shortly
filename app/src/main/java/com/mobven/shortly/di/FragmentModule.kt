package com.mobven.shortly.di

import com.mobven.shortly.adapter.ShortLinkAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FragmentModule {
    @Provides
    @Singleton
    fun provideAdapter(): ShortLinkAdapter {
        return ShortLinkAdapter()
    }
}