package com.example.starwars.domain.usecase

import com.example.starwars.data.model.Film
import com.example.starwars.domain.repository.FilmRepository
import com.example.starwars.domain.repository.Repository

class GetFilmsUseCase (private val repository: FilmRepository) {
    fun execute() = repository.getResultStream()
}