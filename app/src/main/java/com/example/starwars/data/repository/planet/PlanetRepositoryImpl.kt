package com.example.starwars.data.repository.planet

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.starwars.data.api.PlanetAPIService
import com.example.starwars.data.model.Planet
import com.example.starwars.data.repository.NetworkConstants
import com.example.starwars.domain.repository.Repository
import kotlinx.coroutines.flow.Flow

class PlanetRepositoryImpl(
    private val service : PlanetAPIService
): Repository<Planet> {

    override fun getResultStream(): Flow<PagingData<Planet>> {
        return Pager(
            config = PagingConfig(
                pageSize = NetworkConstants.DEFAULT_PAGE_SIZE,
                initialLoadSize = NetworkConstants.DEFAULT_PAGE_SIZE
            ),
            pagingSourceFactory = { PlanetPagingSource(service) }
        ).flow
    }

    override suspend fun getItem(id: String): Planet {
        TODO("Not yet implemented")
    }
}