package com.example.starwars.data.api

import com.example.starwars.data.model.APIResponse
import com.example.starwars.data.model.People
import retrofit2.http.GET
import retrofit2.http.Query

interface PeopleAPIService {

    @GET("people/")
    suspend fun getList(
        @Query("page") page : Int? = null,
        @Query("planet") planetId : Long? = null,
        @Query("film") filmId : Long? = null
    ) : APIResponse<People>
}