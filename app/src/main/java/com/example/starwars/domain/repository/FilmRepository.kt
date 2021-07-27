package com.example.starwars.domain.repository

import com.example.starwars.data.model.APIResponse
import com.example.starwars.data.model.Film

interface FilmRepository: Repository<Film> {

    suspend fun getFilmByPage(page : Int): APIResponse<Film>

}