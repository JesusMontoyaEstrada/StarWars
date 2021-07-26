package com.example.starwars.domain.usecase

import com.example.starwars.data.model.Film
import com.example.starwars.domain.repository.Repository

class GetFilmsUseCase (private val repository: Repository<Film>) {
    fun execute() = repository.getResultStream()
}