package com.example.starwars.domain.usecase

import com.example.starwars.domain.repository.PeopleRepository

class GetPeopleUseCase (private val repository : PeopleRepository) {
    fun execute (planet : Long? = null, film : Long? = null) = repository.getResultStream(planet, film)
}