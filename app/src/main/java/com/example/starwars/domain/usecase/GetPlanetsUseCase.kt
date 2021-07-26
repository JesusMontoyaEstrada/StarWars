package com.example.starwars.domain.usecase

import com.example.starwars.data.model.Planet
import com.example.starwars.domain.repository.Repository

class GetPlanetsUseCase (private val repository : Repository<Planet>) {
    fun execute() = repository.getResultStream()
}