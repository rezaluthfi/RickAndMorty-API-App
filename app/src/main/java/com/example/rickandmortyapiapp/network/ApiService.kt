package com.example.rickandmortyapiapp.network

import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("character")
    fun getAllCharacters(): Call<CharactersResponse>
}
