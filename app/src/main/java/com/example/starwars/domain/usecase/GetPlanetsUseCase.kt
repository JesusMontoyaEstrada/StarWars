package com.example.starwars.domain.usecase

import com.example.starwars.data.model.Planet
import com.example.starwars.domain.repository.PlanetRepository
import com.example.starwars.domain.repository.Repository

class GetPlanetsUseCase (private val repository : PlanetRepository) {
    fun execute() = repository.getResultStream()
}