package com.example.starwars.data.repository.people

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.starwars.data.api.PeopleAPIService
import com.example.starwars.data.model.Film
import com.example.starwars.data.model.People
import com.example.starwars.data.model.Planet
import com.example.starwars.data.repository.NetworkConstants
import com.example.starwars.domain.repository.Repository
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

class PeoplePagingSource(
    private val service : PeopleAPIService,
    private val planet : Long? = null, private val film : Long? = null
) : PagingSource<Int, People>(){
    override fun getRefreshKey(state: PagingState<Int, People>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, People> {
        val page = params.key ?: NetworkConstants.DEFAULT_STARTING_PAGE_INDEX
        return try {

            val response = service.getList(page, planet, film)

            response.results.forEachIndexed { index, people ->
                var newID = page.minus(1)
                newID = newID.times(10)
                people.id = newID.plus(index.plus(1)).toLong()
                people.movies = mutableListOf()
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