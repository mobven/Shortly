package com.example.samplerunproject

import androidx.room.Database
import androidx.room.RoomDatabase

//Veritabanını oluşturduk

@Database(entities = [Result::class], version = 1)
abstract class LinkListDatabase : RoomDatabase() {
    abstract fun listDAO() : LinkDao
}