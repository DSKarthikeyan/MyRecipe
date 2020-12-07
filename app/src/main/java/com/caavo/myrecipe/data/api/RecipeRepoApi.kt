package com.caavo.myrecipe.data.api

import com.caavo.myrecipe.data.model.RecipeDetails
import retrofit2.Response
import retrofit2.http.GET

interface RecipeRepoApi {

    @GET("reciped9d7b8c.json")
    suspend fun getRecipeDetails(): Response<List<RecipeDetails>>

}