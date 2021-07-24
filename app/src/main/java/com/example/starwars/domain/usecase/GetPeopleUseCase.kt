package com.example.starwars.domain.usecase

import com.example.starwars.domain.repository.PeopleRepository

class GetPeopleUseCase (private val repository : PeopleRepository) {
    suspend fun execute () = repository.getResultStream()
}