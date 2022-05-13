package com.mobven.shortly.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mobven.shortly.ShortenData
import kotlinx.coroutines.flow.Flow

@Dao
interface LinkDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLink(vararg shortenData: ShortenData)

    @Query("Select * FROM links")
    fun getLinkList(): Flow<List<ShortenData>>

    @Query("DELETE FROM links WHERE code = :code")
    suspend fun deleteLink(code: String): Int

}