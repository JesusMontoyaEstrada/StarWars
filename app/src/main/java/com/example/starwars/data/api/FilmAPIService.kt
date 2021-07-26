package com.example.starwars.data.api

import com.example.starwars.data.model.APIResponse
import com.example.starwars.data.model.Film
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface FilmAPIService {

    @GET("films/")
    suspend fun getList(
        @Query("page") page : Int? = null,
    ) : APIResponse<Film>

    @GET("films/{id}")
    suspend fun getItem(
        @Path("id") id : Long
    ) : Film
}