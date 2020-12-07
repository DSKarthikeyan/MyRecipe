package com.caavo.myrecipe.data.repository

import com.caavo.myrecipe.data.api.RetrofitInstance
import com.caavo.myrecipe.data.db.RecipeDatabase
import com.caavo.myrecipe.data.model.RecipeDetails

class RecipeRepository(private val recipeDatabase: RecipeDatabase) {

    /**
     * fun: toGet Trending Repos from server using Retrofit
     */
    suspend fun getRecipeDetails() = RetrofitInstance.api.getRecipeDetails()

    /**
     * fun: To Insert List of Repo details to Local DB
     */
    suspend fun upsert(reposDetails: List<RecipeDetails>) =
            recipeDatabase.getRecipeDetailsDAO().upsert(reposDetails)

    /**
     * fun: toGet All Live Trending Repo Details from Local DB
     */
    fun getRecipesFromLocal() = recipeDatabase.getRecipeDetailsDAO().getAllRecipeDetails()
}