package com.mobven.shortly.data.room

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE 'links' ADD COLUMN 'isFavorite' INTEGER NOT NULL DEFAULT 0")
    }
}