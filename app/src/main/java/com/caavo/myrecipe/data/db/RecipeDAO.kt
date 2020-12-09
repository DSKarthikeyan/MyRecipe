package com.caavo.myrecipe.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.caavo.myrecipe.data.model.CartList
import com.caavo.myrecipe.data.model.RecipeDetails

@Dao
interface RecipeDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(objects: List<RecipeDetails?>)

    @Query("SELECT * FROM recipe_details")
    fun getAllRecipeDetails(): LiveData<List<RecipeDetails>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCartList(objects: CartList?)

    @Query("SELECT * FROM cart_details")
    fun getAllCartList(): LiveData<List<CartList>>

    @Query("SELECT EXISTS(SELECT * FROM cart_details where productId is :productId)")
    fun getCartItemById(productId: Int): Boolean

    @Query("Delete from cart_details where productId is :productId")
    fun deleteItemInCart(productId: Int): Int

}