package com.example.starwars.data.repository.film

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.starwars.data.api.FilmAPIService
import com.example.starwars.data.model.APIResponse
import com.example.starwars.data.model.Film
import com.example.starwars.data.model.FilmPage
import com.example.starwars.data.repository.NetworkConstants
import com.example.starwars.domain.repository.FilmRepository
import com.example.starwars.domain.repository.Repository
import com.example.starwars.domain.usecase.GetFilmsByPageUseCase
import kotlinx.coroutines.flow.Flow

class FilmRepositoryImpl(
    private val service : FilmAPIService
    ): FilmRepository{

    var movies : MutableList<FilmPage> = mutableListOf()

    //Check PlanetRepositoryImpl for instructions
    override suspend fun getFilmByPage(page: Int): APIResponse<Film> {
        return movies.find { it.page == page }?.apiResponse ?: run {

            val response = service.getList(page)

            response.results.forEachIndexed { index, film ->
                var newID = page.minus(1)
                newID = newID.times(10)
                film.id = newID.plus(index.plus(1)).toLong()
            }

            movies.add(FilmPage(page, response))

            return@run response
        }
    }

    override fun getResultStream(): Flow<PagingData<Film>> {
        return Pager(
            config = PagingConfig(
                pageSize = NetworkConstants.DEFAULT_PAGE_SIZE,
                initialLoadSize = NetworkConstants.DEFAULT_PAGE_SIZE
            ),
            pagingSourceFactory = { FilmPagingSource(this) }
        ).flow
    }

    //Check PlanetRepositoryImpl for instructions
    override suspend fun getItem(id: Long): Film {
        var film: Film? = null
        movies.forEach {
            film = it.films.firstOrNull { it.id == id }

            film?.let {
                return@forEach
            }
        }

        return film ?: run {
            val film = service.getItem(id)
            film.id = id

            movies.firstOrNull { it.page == null } ?: run {
                val filmPage = FilmPage(null, APIResponse(0, null, null, mutableListOf()))
                movies.add(0, filmPage)

                filmPage
            }.films.add(film)

            return@run film
        }
    }

}