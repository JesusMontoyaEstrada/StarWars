package com.example.starwars.domain.usecase

import com.example.starwars.domain.repository.PlanetRepository

class GetPlanetsByPageUseCase(private val repository: PlanetRepository) {
    suspend fun execute(page : Int) = repository.getPlanetByPage(page)
}