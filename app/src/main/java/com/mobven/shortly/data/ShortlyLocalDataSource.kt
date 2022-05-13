package com.mobven.shortly.data

import com.mobven.shortly.ShortenData
import com.mobven.shortly.data.room.LinkDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ShortlyLocalDataSource {
    fun getLinks(): Flow<List<ShortenData>>
}

class ShortlyLocalDataSourceImpl @Inject constructor(
    private val linkDao: LinkDao
) : ShortlyLocalDataSource {
    override fun getLinks(): Flow<List<ShortenData>> = linkDao.getLinkList()
}