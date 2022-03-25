package com.restapi.testeapirest.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Api {
        val retrofit =  Retrofit.Builder()
            .baseUrl("https://api.thedogapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        fun InterfaceApi(): InterfaceApi {
            return retrofit.create(InterfaceApi::class.java)
        }

}