package com.mobven.shortly.di

import android.app.Application
import androidx.room.Room
import com.mobven.shortly.data.room.LinkDao
import com.mobven.shortly.data.room.MIGRATION_1_2
import com.mobven.shortly.data.room.ShortlyDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(application: Application, callback: ShortlyDatabase.Callback): ShortlyDatabase{
        return Room.databaseBuilder(application, ShortlyDatabase::class.java, "shortly_database")
            .fallbackToDestructiveMigration()
            .addCallback(callback)
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    @Provides
    fun provideArticleDao(db: ShortlyDatabase): LinkDao{
        return db.getLinkDao()
    }

}