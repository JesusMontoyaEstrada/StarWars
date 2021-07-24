package com.example.starwars.data.repository.people

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.starwars.data.api.PeopleAPIService
import com.example.starwars.data.model.Film
import com.example.starwars.data.model.People
import com.example.starwars.data.model.Planet
import com.example.starwars.data.repository.NetworkConstants
import com.example.starwars.data.repository.film.FilmPagingSource
import com.example.starwars.domain.repository.PeopleRepository
import com.example.starwars.domain.repository.Repository
import kotlinx.coroutines.flow.Flow

class PeopleRepositoryImpl(
    private val service : PeopleAPIService
) : PeopleRepository{

    override fun getResultStream(planet: Long?, film: Long?
//                                 , planetList: List<Planet>, filmList : List<Film>,
//                                 planetRepo: Repository<Planet>, filmRepo: Repository<Film>
    ): Flow<PagingData<People>> {
        return Pager(
            config = PagingConfig(
                pageSize = NetworkConstants.DEFAULT_PAGE_SIZE,
                initialLoadSize = NetworkConstants.DEFAULT_PAGE_SIZE
            ),
            pagingSourceFactory = { PeoplePagingSource(service, planet, film
//                , planetList, filmList, planetRepo, filmRepo
            ) }
        ).flow
    }
}