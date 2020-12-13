package com.caavo.myrecipe.ui.cartDetails

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.caavo.myrecipe.R
import com.caavo.myrecipe.adapter.CartListAdapter
import com.caavo.myrecipe.data.db.RecipeDatabase
import com.caavo.myrecipe.data.model.CartList
import com.caavo.myrecipe.data.model.RecipeDetails
import com.caavo.myrecipe.data.repository.RecipeRepository
import com.caavo.myrecipe.ui.RecipeDetailsImpl
import com.caavo.myrecipe.ui.recipeDetails.RecipeDetailsActivity
import com.caavo.myrecipe.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CartDetailsActivity : AppCompatActivity(), RecipeDetailsImpl {

    lateinit var cartListViewModel: CartViewModel
    lateinit var cartListAdapter: CartListAdapter
    lateinit var cartListRecyclerView: RecyclerView
    lateinit var progressBarCircular: ProgressBar
    private lateinit var textViewStatus: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cart_activity)
        initializeViews()
    }

    /**
     * fun: initialize View Objects
     */
    private fun initializeViews() {
        progressBarCircular = findViewById(R.id.progressCircularBar)
        textViewStatus = findViewById(R.id.textViewStatus)
        cartListRecyclerView = findViewById(R.id.recyclerViewCartList)

        initCartListView()
    }

    /**
     * fun: initialize and load Repo Details View
     */
    private fun initCartListView() {
        val recipeListDatabase = RecipeDatabase(this)
        val recipeRepository = RecipeRepository(recipeListDatabase)
        val viewModelProviderFactory = CartVMProviderFactory(
            recipeRepository
        )
        cartListViewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(CartViewModel::class.java)


        cartListViewModel.cartList.observe(this, { response ->
            if (response.isNotEmpty()) {
                cartListAdapter.differ.submitList(response)
            } else {
                showLoadingText("Cart is empty")
            }
        })

        setupRecyclerView()
    }

    /**
     * fun: setup Recycler View
     */
    private fun setupRecyclerView() {
        cartListAdapter = CartListAdapter(this)
        cartListRecyclerView.apply {
            adapter = cartListAdapter
            layoutManager = LinearLayoutManager(this@CartDetailsActivity)
        }
    }

    override fun buttonClickListenerRemoveFromCart(cartList: CartList) {
        GlobalScope.launch(Dispatchers.Main) {
            cartListViewModel.apply {
                removeCartData(cartList)
            }
        }
    }

    /**
     * fun: ShowText based on Loader
     */
    private fun showLoadingText(message: String) {
        if (message.isNotEmpty()) {
            cartListRecyclerView.visibility = View.INVISIBLE
            textViewStatus.visibility = View.VISIBLE
            textViewStatus.text = message
        }
    }

    /** Called when the user taps the Send button */
    private fun openRecipeDetailsActivity() {
        val intent = Intent(this, RecipeDetailsActivity::class.java)
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        openRecipeDetailsActivity()
    }

}