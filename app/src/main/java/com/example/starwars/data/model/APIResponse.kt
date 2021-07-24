package com.example.starwars.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class APIResponse<T : Any> (

    @SerializedName("count")
    var count : Int,

    @SerializedName("next")
    var next : String?,

    @SerializedName("previous")
    var previous : String?,

    @SerializedName("results")
    var results: MutableList<T>

    ) : Serializable