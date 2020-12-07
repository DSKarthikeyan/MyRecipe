 package com.caavo.myrecipe.ui.recipeDetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.caavo.myrecipe.R
import com.caavo.myrecipe.RecipeApplication
import com.caavo.myrecipe.adapter.RecipeListAdapter
import com.caavo.myrecipe.data.db.RecipeDatabase
import com.caavo.myrecipe.data.repository.RecipeRepository
import com.caavo.myrecipe.util.Resource

 class RecipeDetailsActivity : AppCompatActivity() {

     lateinit var recipeViewModel: RecipeViewModel
     lateinit var recipeListAdapter:  RecipeListAdapter
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
         val trendingRepoDatabase = RecipeDatabase(this)
         val trendingRepoRepository = RecipeRepository(trendingRepoDatabase)
         val viewModelProviderFactory = RecipeVMProviderFactory(
             application as RecipeApplication,
             trendingRepoRepository
         )
         recipeViewModel =
             ViewModelProvider(this, viewModelProviderFactory).get(RecipeViewModel::class.java)

         recipeViewModel.getTrendingRepoFromLocal().observe(this) { response ->
             if (response.isNotEmpty()) {
                 recipeListAdapter.differ.submitList(response)
             } else {
                 recipeViewModel.getTrendingRepo()
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
         recipeListAdapter = RecipeListAdapter()
         recipeListoRecyclerView.apply {
             adapter = recipeListAdapter
             layoutManager =  GridLayoutManager(this@RecipeDetailsActivity,2)
//             setDivider(R.drawable.solid_line)
         }
     }

     /**
      * fun: load item divider in recycler view
      */
     private fun RecyclerView.setDivider(@DrawableRes drawableRes: Int) {
         val divider = DividerItemDecoration(
             this.context,
             DividerItemDecoration.VERTICAL
         )
         val drawable = ContextCompat.getDrawable(
             this.context,
             drawableRes
         )
         drawable?.let {
             divider.setDrawable(it)
             addItemDecoration(divider)
         }
     }

//     /**
//      * fun: option Menu Handler in Toolbar
//      */
//     override fun onCreateOptionsMenu(menu: Menu): Boolean {
//         menuInflater.inflate(R.menu.search_bar, menu)
//         return true
//     }
//
//     override fun onOptionsItemSelected(item: MenuItem): Boolean {
//         val id: Int = item.itemId
//         return if (id == R.id.action_search) {
//             true
//         } else super.onOptionsItemSelected(item)
//     }
//

}