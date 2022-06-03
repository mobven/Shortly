package com.mobven.shortly.di

import com.mobven.shortly.adapter.ShortLinkAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module
@InstallIn(FragmentComponent::class)
object FragmentModule {

    @Provides
    fun provideAdapter(): ShortLinkAdapter {
        return ShortLinkAdapter()
    }
}