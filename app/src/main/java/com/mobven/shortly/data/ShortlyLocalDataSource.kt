package com.mobven.shortly.data

import com.mobven.shortly.ShortenData
import com.mobven.shortly.data.room.LinkDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ShortlyLocalDataSource {
    fun getLinks(): Flow<List<ShortenData>>
    suspend fun insertLink(shortenData: ShortenData)
    suspend fun updateSelected(isSelected: Boolean, code: String)
    suspend fun getOldSelected():String?
}

class ShortlyLocalDataSourceImpl @Inject constructor(
    private val linkDao: LinkDao
) : ShortlyLocalDataSource {
    override fun getLinks(): Flow<List<ShortenData>> = linkDao.getLinkList()
    override suspend fun insertLink(shortenData: ShortenData) = linkDao.insertLink(shortenData)
    override suspend fun updateSelected(isSelected: Boolean, code: String) = linkDao.updateSelected(isSelected, code)
    override suspend fun getOldSelected(): String? = linkDao.getOldSelected()
}