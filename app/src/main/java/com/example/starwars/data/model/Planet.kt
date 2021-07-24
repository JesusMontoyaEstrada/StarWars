package com.example.starwars.data.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Planet(

    @Expose
    var id : Long,

    @SerializedName("name")
    var name : String,

    @SerializedName("url")
    var url : String

) : Serializable
