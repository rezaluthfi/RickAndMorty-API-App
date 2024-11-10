package com.example.rickandmortyapiapp.model

import com.google.gson.annotations.SerializedName

data class Characters(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("status")
    val status: String,

    @SerializedName("species")
    val species: String,

    @SerializedName("gender")
    val gender: String
)
