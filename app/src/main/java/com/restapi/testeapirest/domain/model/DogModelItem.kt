package com.restapi.testeapirest.domain.model

data class DogModelItem(
    val breeds: List<Breed>,
    val height: Int,
    val id: String,
    val url: String,
    val width: Int
)