package com.caavo.myrecipe.ui.recipeDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.caavo.myrecipe.RecipeApplication
import com.caavo.myrecipe.data.repository.RecipeRepository

class RecipeVMProviderFactory(
    private val application: RecipeApplication,
    private val recipeRepository: RecipeRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return RecipeViewModel(application,recipeRepository) as T
    }
}