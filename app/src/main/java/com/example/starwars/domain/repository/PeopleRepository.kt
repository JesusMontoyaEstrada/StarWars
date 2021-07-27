package com.example.starwars.domain.repository

import androidx.paging.PagingData
import com.example.starwars.data.model.APIResponse
import com.example.starwars.data.model.Film
import com.example.starwars.data.model.People
import com.example.starwars.data.model.Planet
import kotlinx.coroutines.flow.Flow

interface PeopleRepository {

    fun getResultStream(planet : Long? = null, film : Long? = null) : Flow<PagingData<People>>

    suspend fun getPeopleByPage(page: Int, planet: Long? = null, film: Long? = null): APIResponse<People>
}