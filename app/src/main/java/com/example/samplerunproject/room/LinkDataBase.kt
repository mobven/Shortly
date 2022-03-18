package com.example.samplerunproject

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.samplerunproject.room.LinkDao

//Veritabanını oluşturduk

@Database(entities = [Result::class], version = 3)
abstract class LinkListDatabase : RoomDatabase() {
    abstract fun listDAO() : LinkDao

    companion object {
    @Volatile
    private var INSTANCE: LinkListDatabase? = null

    //TODO we need to add migration later

    fun getDatabase(context: Context) : LinkListDatabase{

       return when(val tempInstance = INSTANCE){
            null -> synchronized(this){
                Room.databaseBuilder(context, LinkListDatabase::class.java, "linkList").
                fallbackToDestructiveMigration().build()
            }
           else -> tempInstance
        }


    }
        }
    //Room.databaseBuilder(applicationContext, LinkListDatabase::class.java, "linkList").fallbackToDestructiveMigration().allowMainThreadQueries().build()
}