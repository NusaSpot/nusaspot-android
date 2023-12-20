package com.jpmedia.nusaspot.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Retro {
    fun getRetroClientInstance(): Retrofit{
        val gson = GsonBuilder().setLenient().create()
        return Retrofit.Builder()
            .baseUrl("https://nutricare-api-4m337nn6bq-et.a.run.app/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}