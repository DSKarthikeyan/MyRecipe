package com.caavo.myrecipe.ui.recipeDetails

import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import android.util.Log
import androidx.lifecycle.*
import com.caavo.myrecipe.RecipeApplication
import com.caavo.myrecipe.data.model.CartList
import com.caavo.myrecipe.data.model.RecipeDetails
import com.caavo.myrecipe.data.repository.RecipeRepository
import com.caavo.myrecipe.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.Response

class RecipeViewModel(
    application: RecipeApplication,
    private val recipeRepository: RecipeRepository
) : AndroidViewModel(application) {

    val recipeList: MutableLiveData<Resource<List<RecipeDetails>>> = MutableLiveData()

    /**
     * fun: to get Trending Repo details from server
     */
    fun getRecipes() = viewModelScope.launch {
        getRecipesFromServer()
    }

    /**
     * fun: get trending repo from local database through repository
     */
    fun getRecipesFromLocal() = recipeRepository.getRecipesFromLocal()

    /**
     * fun: get trending repo from server
     */
    private suspend fun getRecipesFromServer() {
        try {
            if (hasInternetConnection()) {
                recipeList.postValue(Resource.Loading())
                val response = recipeRepository.getRecipeDetails()
                recipeList.postValue(handleRecipeResponse(response))
            } else {
                recipeList.postValue(Resource.Error("No Internet Connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> recipeList.postValue(Resource.Error("No Internet Connection"))
                else -> recipeList.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    suspend fun insertCartData(cartData: CartList):Long {
        var isInsertedInLocal: Long = -1
        viewModelScope.launch {
           isInsertedInLocal =  recipeRepository.insertCartList(cartData)
        }
        return isInsertedInLocal;
    }

    /**
     * fun: get Cart Item by productId from LocalDB
     */
    suspend fun getCartItemById(productId: Int): CartList {

            return recipeRepository.getCartItemById(productId)
//            Log.d("DSK "," isincart1 $id")
    }

    /**
     * fun: toHandle RepoDetails input and insert to LocalDB
     *    and to return if Server fetched response is successful
     */
    private fun handleRecipeResponse(response: Response<List<RecipeDetails>>): Resource<List<RecipeDetails>>? {
        if (response.isSuccessful) {
            response.body()?.let { resultResponse ->
                viewModelScope.launch(Dispatchers.IO) {
                    recipeRepository.upsert(resultResponse)
                }
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    /**
     * fun: to handle Internet Connectivity
     */
    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<RecipeApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }

}