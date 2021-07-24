package com.example.starwars.data.api

import com.example.starwars.data.model.APIResponse
import com.example.starwars.data.model.Planet
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PlanetAPIService {

    @GET("planets/")
    suspend fun getList(
        @Query("page") page : Int? = null,
    ) : APIResponse<Planet>

    @GET("{id}")
    suspend fun getItem(
        @Path("id") id : String
    ) : Planet

}