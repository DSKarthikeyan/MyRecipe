package com.caavo.myrecipe.ui.cartDetails

import androidx.lifecycle.*
import com.caavo.myrecipe.data.model.CartList
import com.caavo.myrecipe.data.repository.RecipeRepository
import com.caavo.myrecipe.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CartViewModel(
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    var cartList: LiveData<List<CartList>> = MutableLiveData()

    init {
        cartList =  getCartDetails()
    }

    /**
     * fun: get trending repo from server
     */
    private fun getCartDetails(): LiveData<List<CartList>> {
        return recipeRepository.getCartDetails()
    }

     fun removeCartData(cartData: CartList) {
        viewModelScope.launch(Dispatchers.IO) {
            recipeRepository.deleteItemInCart(cartData.productId)
        }
    }
}
