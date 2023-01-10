package com.mobven.shortly.data

import com.mobven.shortly.ShortenData
import com.mobven.shortly.data.room.LinkDao
import javax.inject.Inject

interface ShortlyLocalDataSource {
    suspend fun insertLink(shortenData: ShortenData)
    suspend fun updateSelected(isSelected: Boolean, code: String)
    suspend fun updateFavorite(isFavorite: Boolean, code: String)
    suspend fun getOldSelected():String?
    suspend fun deleteLink(code: String): Int
}

class ShortlyLocalDataSourceImpl @Inject constructor(
    private val linkDao: LinkDao
) : ShortlyLocalDataSource {
    override suspend fun insertLink(shortenData: ShortenData) = linkDao.insertLink(shortenData)
    override suspend fun updateSelected(isSelected: Boolean, code: String) = linkDao.updateSelected(isSelected, code)
    override suspend fun updateFavorite(isFavorite: Boolean, code: String) = linkDao.updateFavorite(isFavorite, code)
    override suspend fun getOldSelected(): String? = linkDao.getOldSelected()
    override suspend fun deleteLink(code: String): Int = linkDao.deleteLink(code)

}