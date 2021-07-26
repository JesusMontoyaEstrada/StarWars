package com.example.starwars.domain.usecase

import com.example.starwars.data.model.Planet
import com.example.starwars.domain.repository.Repository

class GetPlanetUseCase (private val repository : Repository<Planet>) {
    suspend fun execute(planet : Long) = repository.getItem(planet)
}