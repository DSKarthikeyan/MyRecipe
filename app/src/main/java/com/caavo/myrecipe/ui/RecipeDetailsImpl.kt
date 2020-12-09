package com.caavo.myrecipe.ui

import com.caavo.myrecipe.data.model.CartList
import com.caavo.myrecipe.data.model.RecipeDetails

interface RecipeDetailsImpl {

    fun buttonClickListenerAddToCart(recipeDetails: RecipeDetails) {}
    fun buttonClickListenerRemoveFromCart(cartList: CartList) {}

}