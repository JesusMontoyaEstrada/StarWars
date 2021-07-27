package com.example.starwars.data.repository.film

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.starwars.data.model.Film
import com.example.starwars.data.repository.NetworkConstants
import com.example.starwars.domain.repository.FilmRepository
import com.example.starwars.domain.usecase.GetFilmsByPageUseCase
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception

class FilmPagingSource(
    private val filmRepository : FilmRepository
) : PagingSource<Int, Film>(){
    override fun getRefreshKey(state: PagingState<Int, Film>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Film> {
        val page = params.key ?: NetworkConstants.DEFAULT_STARTING_PAGE_INDEX
        return try {

            val response = filmRepository.getFilmByPage(page)

            val nextKey = if (response.next == null) {
                null
            } else {
                page + (params.loadSize / NetworkConstants.DEFAULT_STARTING_PAGE_INDEX)
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