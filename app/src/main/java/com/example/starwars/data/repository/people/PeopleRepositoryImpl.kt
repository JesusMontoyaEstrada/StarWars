package com.example.starwars.data.repository.people

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.starwars.data.api.PeopleAPIService
import com.example.starwars.data.model.APIResponse
import com.example.starwars.data.model.People
import com.example.starwars.data.repository.NetworkConstants
import com.example.starwars.domain.repository.FilmRepository
import com.example.starwars.domain.repository.PeopleRepository
import com.example.starwars.domain.repository.PlanetRepository
import kotlinx.coroutines.flow.Flow

class PeopleRepositoryImpl(
    private val service : PeopleAPIService,
    private val planetRepository : PlanetRepository,
    private val filmRepository: FilmRepository
) : PeopleRepository{

    override fun getResultStream(planet: Long?, film: Long?
    ): Flow<PagingData<People>> {
        return Pager(
            config = PagingConfig(
                pageSize = NetworkConstants.DEFAULT_PAGE_SIZE,
                initialLoadSize = NetworkConstants.DEFAULT_PAGE_SIZE
            ),
            pagingSourceFactory = { PeoplePagingSource(this, planet, film ) }
        ).flow
    }


    //Intersect peopleResponse to assign planet object and movies object list info and return a complete full people list
    override suspend fun getPeopleByPage(
        page: Int,
        planet: Long?,
        film: Long?
    ): APIResponse<People> {
        val response = service.getList(page, planet, film)

        response.results.forEachIndexed { index, people ->
            var newID = page.minus(1)
            newID = newID.times(10)
            people.id = newID.plus(index.plus(1)).toLong()
            people.movies = mutableListOf()

            val planetID = people.homeworld.filter { it.isDigit() }
            val planet = planetRepository.getItem(planetID.toLong())
            people.planet = planet

            people.movies = people.films.map { filmUrl ->
                val filmID = filmUrl.filter { it.isDigit() }
                filmRepository.getItem(filmID.toLong())
            }.toMutableList()

        }

        return response
    }
}