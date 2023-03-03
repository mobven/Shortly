package com.mobven.shortly.di

import com.mobven.shortly.analytics.AnalyticsManager
import com.mobven.shortly.analytics.AnalyticsManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ManagerModule {

    @Binds
    abstract fun bindAnalyticsManager(analyticsManagerImpl: AnalyticsManagerImpl): AnalyticsManager
}