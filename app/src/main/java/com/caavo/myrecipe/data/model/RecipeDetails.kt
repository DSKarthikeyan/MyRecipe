package com.caavo.myrecipe.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "recipe_details", indices = [Index(value = ["productId"], unique = true)])
data class RecipeDetails(
    val category: String,
    val description: String,
    @SerializedName(value = "id")
    val productId: Int,
    val image: String,
    val label: String,
    val name: String,
    val price: String
) {
    @PrimaryKey(autoGenerate = true)
    var tableId: Int? = null
}