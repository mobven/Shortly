package com.mobven.shortly.data

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mobven.shortly.ShortenData
import com.mobven.shortly.data.room.LinkDao

/*
class ShortLinkPagingDataSource (private val linkDao: LinkDao):
    PagingSource<Int, ShortenData>() {
    private val firstPage = 1

    override fun getRefreshKey(state: PagingState<Int, ShortenData>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ShortenData> {
        val page = params.key ?: firstPage
        return try {
            val shortLink = linkDao.getLinkList()
            LoadResult.Page(
                data = shortLink,
                prevKey = if (page == firstPage) null else page.minus(1),
                nextKey = if (shortLink.isNullOrEmpty()) null else page.plus(1)
            )
        }
        catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
} */