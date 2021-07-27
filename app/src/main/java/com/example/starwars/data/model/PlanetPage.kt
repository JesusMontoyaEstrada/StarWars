package com.example.starwars.data.model

data class PlanetPage(
    var page : Int?,
    var apiResponse: APIResponse<Planet>
){
    var planets: MutableList<Planet>
    get(){
        return  apiResponse.results
    }
    set(value){
        apiResponse.results = value
    }
}