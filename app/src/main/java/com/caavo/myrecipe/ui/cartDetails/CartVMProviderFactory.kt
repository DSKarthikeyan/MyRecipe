package com.caavo.myrecipe.ui.cartDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.caavo.myrecipe.RecipeApplication
import com.caavo.myrecipe.data.repository.RecipeRepository

class CartVMProviderFactory(
    private val recipeRepository: RecipeRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CartViewModel(recipeRepository) as T
    }
}