package com.example.samplerunproject

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.samplerunproject.room.LinkDao

//Veritabanını oluşturduk

@Database(entities = [Result::class], version = 2)
abstract class LinkListDatabase : RoomDatabase() {
    abstract fun listDAO() : LinkDao
}