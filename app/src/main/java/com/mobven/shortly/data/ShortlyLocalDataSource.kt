package com.mobven.shortly.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.mobven.shortly.ShortenData
import com.mobven.shortly.data.room.LinkDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ShortlyLocalDataSource {
    fun getLinksFlow(): Flow<List<ShortenData>>
    fun getLinksPagingDataFlow(): Flow<PagingData<ShortenData>>
    suspend fun insertLink(shortenData: ShortenData)
    suspend fun updateSelected(isSelected: Boolean, code: String)
    suspend fun getOldSelected(): String?
    suspend fun updateFavorite(isFavorite: Boolean, code: String)
    suspend fun deleteLink(code: String): Int
}

class ShortlyLocalDataSourceImpl @Inject constructor(
    private val linkDao: LinkDao
) : ShortlyLocalDataSource {
    override fun getLinksFlow() = linkDao.getLinkListFlow()
    override suspend fun insertLink(shortenData: ShortenData) = linkDao.insertLink(shortenData)
    override suspend fun updateSelected(isSelected: Boolean, code: String) = linkDao.updateSelected(isSelected, code)
    override suspend fun updateFavorite(isFavorite: Boolean, code: String) = linkDao.updateFavorite(isFavorite, code)
    override suspend fun getOldSelected(): String? = linkDao.getOldSelected()
    override suspend fun deleteLink(code: String): Int = linkDao.deleteLink(code)

    override fun getLinksPagingDataFlow() = Pager(
        config = PagingConfig(pageSize = PAGE_SIZE),
        pagingSourceFactory = { linkDao.getLinkListPagingSource() }
    ).flow

    companion object {
        private const val PAGE_SIZE = 20
    }
}