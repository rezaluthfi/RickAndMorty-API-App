package com.example.rickandmortyapiapp.network

import com.example.rickandmortyapiapp.model.Characters
import com.google.gson.annotations.SerializedName

data class CharactersResponse(
    @SerializedName("results")
    val results: List<Characters>
)

