package com.example.samplerunproject.di

import android.content.Context
import androidx.room.Room
import com.example.samplerunproject.LinkListDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun injectShortDao(database: LinkListDatabase) = database.listDAO()

    @Provides
    @Singleton
    fun injectShortDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, LinkListDatabase::class.java, "linkList")
            .fallbackToDestructiveMigration().build()


}