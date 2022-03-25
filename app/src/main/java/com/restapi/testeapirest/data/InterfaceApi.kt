package com.restapi.testeapirest.data

import com.restapi.testeapirest.domain.model.DogModelItem
import retrofit2.Call
import retrofit2.http.GET

interface InterfaceApi {

    @GET("v1/images/search?api_key=ee03aeeb-8515-45d4-b5ff-19bc17c6d6f4")
    fun getDogs(): Call<List<DogModelItem>>
}
