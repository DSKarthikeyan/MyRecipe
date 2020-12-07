package com.caavo.myrecipe.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.caavo.myrecipe.data.model.RecipeDetails

@Dao
interface RecipeDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    suspend fun upsert(objects: List<RecipeDetails?>)

    @Query("SELECT * FROM recipe_details")
    fun getAllRecipeDetails(): LiveData<List<RecipeDetails>>

}