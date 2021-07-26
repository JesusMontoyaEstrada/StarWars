package com.example.starwars.data.repository.planet

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.starwars.data.api.PlanetAPIService
import com.example.starwars.data.model.Planet
import com.example.starwars.data.repository.NetworkConstants
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

class PlanetPagingSource(
    private val service : PlanetAPIService
) : PagingSource<Int, Planet>(){

    override fun getRefreshKey(state: PagingState<Int, Planet>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Planet> {
        val page = params.key ?: NetworkConstants.DEFAULT_STARTING_PAGE_INDEX
        return try {

            val response = service.getList(page)

            response.results.forEachIndexed { index, planet ->
                var newID = page.minus(1)
                newID = newID.times(10)
                planet.id = newID.plus(index.plus(1)).toLong()
            }

            val nextKey = if (response.next == null) {
                null
            } else {
                page + 1
            }
            LoadResult.Page(
                data = response.results,
                prevKey = if (page == NetworkConstants.DEFAULT_STARTING_PAGE_INDEX) null else page - 1,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }  catch (ex: Exception) {
            LoadResult.Error(ex)
        }
    }
}