package com.example.starwars.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class People(

    @SerializedName("name")
    var name : String,

    @SerializedName("gender")
    var gender : String,

    @SerializedName("homeworld")
    var homeworld : String,

    @SerializedName("films")
    var films : List<String>,

    @Expose
    var planet : Planet?,

    @Expose
    var movies : MutableList<Film?>

): Serializable
