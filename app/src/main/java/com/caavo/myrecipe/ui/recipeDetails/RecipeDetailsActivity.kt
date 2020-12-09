package com.caavo.myrecipe.ui.recipeDetails

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.caavo.myrecipe.R
import com.caavo.myrecipe.RecipeApplication
import com.caavo.myrecipe.adapter.RecipeListAdapter
import com.caavo.myrecipe.data.db.RecipeDatabase
import com.caavo.myrecipe.data.model.CartList
import com.caavo.myrecipe.data.model.RecipeDetails
import com.caavo.myrecipe.data.repository.RecipeRepository
import com.caavo.myrecipe.ui.RecipeDetailsImpl
import com.caavo.myrecipe.ui.cartDetails.CartDetailsActivity
import com.caavo.myrecipe.util.Resource
import kotlinx.coroutines.*

class RecipeDetailsActivity : AppCompatActivity(), RecipeDetailsImpl {

    lateinit var recipeViewModel: RecipeViewModel
    lateinit var recipeListAdapter: RecipeListAdapter
    lateinit var recipeListoRecyclerView: RecyclerView
    lateinit var progressBarCircular: ProgressBar
    private lateinit var textViewStatus: TextView
    private lateinit var buttonTryAgain: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recipe_activity)
        initializeViews()
    }

    /**
     * fun: initialize View Objects
     */
    private fun initializeViews() {
        recipeListoRecyclerView = findViewById(R.id.recyclerViewRecipeList)
        progressBarCircular = findViewById(R.id.progressCircularBar)
        textViewStatus = findViewById(R.id.textViewStatus)
        buttonTryAgain = findViewById(R.id.buttonTryAgain)

        initTrendingRepoView()

        buttonTryAgain.setOnClickListener { recipeViewModel.getTrendingRepo() }
    }

    /**
     * fun: initialize and load Repo Details View
     */
    private fun initTrendingRepoView() {
        val recipeRepoDatabase = RecipeDatabase(this)
        val recipeRepository = RecipeRepository(recipeRepoDatabase)
        val viewModelProviderFactory = RecipeVMProviderFactory(
            application as RecipeApplication,
            recipeRepository
        )
        recipeViewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(RecipeViewModel::class.java)

        recipeViewModel.getRecipesFromLocal().observe(this) { response ->
            if (response.isNotEmpty()) {
                recipeListAdapter.differ.submitList(response)
            } else {
                recipeViewModel.getRecipes()
            }
        }

        recipeViewModel.recipeList.observe(this, { response ->
            when (response) {
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { newsResponse ->
                        recipeListAdapter.differ.submitList(newsResponse)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        showLoadingText(message)
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }
        })

        setupRecyclerView()
    }

    /**
     * fun: Hide loading Progress Bar
     */
    private fun hideProgressBar() {
        recipeListoRecyclerView.visibility = View.VISIBLE
        progressBarCircular.visibility = View.INVISIBLE
        textViewStatus.visibility = View.INVISIBLE
        buttonTryAgain.visibility = View.INVISIBLE
    }

    /**
     * fun: show progress bar
     */
    private fun showProgressBar() {
        recipeListoRecyclerView.visibility = View.INVISIBLE
        progressBarCircular.visibility = View.VISIBLE
        showLoadingText(resources.getString(R.string.text_loading))
        buttonTryAgain.visibility = View.INVISIBLE
    }

    /**
     * fun: ShowText based on Loader
     */
    private fun showLoadingText(message: String) {
        if (message.isNotEmpty()) {
            recipeListoRecyclerView.visibility = View.INVISIBLE
            textViewStatus.visibility = View.VISIBLE
            textViewStatus.text = message
            buttonTryAgain.visibility = View.VISIBLE
        }
    }

    /**
     * fun: setup Recycler View
     */
    private fun setupRecyclerView() {
        recipeListAdapter = RecipeListAdapter(this,recipeViewModel)
        recipeListoRecyclerView.apply {
            adapter = recipeListAdapter
            layoutManager = GridLayoutManager(this@RecipeDetailsActivity, 2)
        }
    }

    override fun buttonClickListenerAddToCart(recipeDetails : RecipeDetails) {
//        Toast.makeText(this, "Clicked: ${recipeDetails.name}", Toast.LENGTH_LONG).show()
        GlobalScope.launch (Dispatchers.Main){
            recipeViewModel.apply {
                val cartList= CartList(recipeDetails.productId,recipeDetails.image,recipeDetails.name,recipeDetails.price)
                insertCartData(cartList)
            }
        }
    }

    /** Called when the user taps the Send button */
    private fun openCartDetailsActivity() {
        val intent = Intent(this, CartDetailsActivity::class.java)
        startActivity(intent)
    }

    /**
     * fun: option Menu Handler in Toolbar
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_cart -> {
                openCartDetailsActivity()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}