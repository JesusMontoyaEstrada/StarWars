package com.example.starwars.domain.usecase

import com.example.starwars.data.model.Film
import com.example.starwars.domain.repository.Repository

class GetFilmUseCase(private val repository : Repository<Film>) {
    suspend fun execute(film : Long) = repository.getItem(film)
}