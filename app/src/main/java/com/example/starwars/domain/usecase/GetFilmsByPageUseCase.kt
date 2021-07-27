package com.example.starwars.domain.usecase

import com.example.starwars.domain.repository.FilmRepository

class GetFilmsByPageUseCase(private val repository : FilmRepository) {
    suspend fun execute(page : Int) = repository.getFilmByPage(page)
}