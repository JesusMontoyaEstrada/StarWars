package com.example.starwars.data.repository.film

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.starwars.data.api.FilmAPIService
import com.example.starwars.data.model.Film
import com.example.starwars.data.repository.NetworkConstants
import com.example.starwars.domain.repository.Repository
import kotlinx.coroutines.flow.Flow

class FilmRepositoryImpl(
    private val service : FilmAPIService
    ): Repository<Film> {

    override fun getResultStream(): Flow<PagingData<Film>> {
        return Pager(
            config = PagingConfig(
                pageSize = NetworkConstants.DEFAULT_PAGE_SIZE,
                initialLoadSize = NetworkConstants.DEFAULT_PAGE_SIZE
            ),
            pagingSourceFactory = { FilmPagingSource(service) }
        ).flow
    }

    override suspend fun getItem(id: String): Film {
        TODO("Not yet implemented")
    }

}