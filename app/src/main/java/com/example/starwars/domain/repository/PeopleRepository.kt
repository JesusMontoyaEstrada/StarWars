package com.example.starwars.domain.repository

import androidx.paging.PagingData
import com.example.starwars.data.model.Film
import com.example.starwars.data.model.People
import com.example.starwars.data.model.Planet
import kotlinx.coroutines.flow.Flow

interface PeopleRepository {

    fun getResultStream(planet : Long? = null, film : Long? = null
//                        , planetList: List<Planet>, filmList : List<Film>,
//                        planetRepo: Repository<Planet>, filmRepo: Repository<Film>
            ) : Flow<PagingData<People>>
}