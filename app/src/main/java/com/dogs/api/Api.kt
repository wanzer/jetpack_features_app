package com.dogs.api

import com.dogs.model.DogBreed
import io.reactivex.Single
import retrofit2.http.GET

interface Api {

    @GET("DevTides/DogsApi/master/dogs.json")
    fun getDogs(): Single<List<DogBreed>>
}