package com.jpmedia.nusaspot.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Retro {
    fun getRetroClientInstance(): Retrofit{
        val gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder()
            .baseUrl("https://nusaspot-api.ramdanisyaputra.my.id/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}