package com.example.starwars.domain.repository

import com.example.starwars.data.model.APIResponse
import com.example.starwars.data.model.Planet

interface PlanetRepository: Repository<Planet> {
    suspend fun getPlanetByPage(page : Int) : APIResponse<Planet>
}