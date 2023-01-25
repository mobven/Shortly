package com.mobven.shortly.data.room

import androidx.paging.PagingSource
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

    @Query("Select * FROM links WHERE original_link LIKE '%' || :search || '%'")
    fun getLinkListPagingSource(search:String): PagingSource<Int, ShortenData>

    @Query("Select * FROM links")
    fun getLinkListFlow(): Flow<List<ShortenData>>

    @Query("DELETE FROM links WHERE code = :code")
    suspend fun deleteLink(code: String): Int

    @Query("UPDATE links SET isSelected = :isSelected WHERE code = :code")
    suspend fun updateSelected(isSelected: Boolean, code: String)

    @Query("Select code FROM links WHERE isSelected = 1")
    suspend fun getOldSelected(): String?

}