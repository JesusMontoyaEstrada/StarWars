package com.example.starwars.domain.usecase

import com.example.starwars.domain.repository.PeopleRepository

class GetPeopleByPageUseCase(private val repository: PeopleRepository) {
    suspend fun execute(page : Int) = repository.getPeopleByPage(page)
}