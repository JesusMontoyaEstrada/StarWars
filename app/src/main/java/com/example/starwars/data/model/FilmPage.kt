package com.example.starwars.data.model

class FilmPage(
    var page: Int?,
    var apiResponse: APIResponse<Film>
) {
    var films : MutableList<Film>
    get(){
        return apiResponse.results
    }
    set(value){
        apiResponse.results = value
    }
}