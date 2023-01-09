package com.mobven.shortly.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mobven.shortly.ShortenData
import com.mobven.shortly.data.converter.ImageConverter
import com.mobven.shortly.di.ApplicationScope
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [ShortenData::class], version = 2)
@TypeConverters(ImageConverter::class)
abstract class ShortlyDatabase : RoomDatabase() {

    abstract fun getLinkDao(): LinkDao

    class Callback @Inject constructor(
        private val database: Provider<ShortlyDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback()
}