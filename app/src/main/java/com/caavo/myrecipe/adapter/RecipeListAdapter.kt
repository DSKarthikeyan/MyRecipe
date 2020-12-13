package com.caavo.myrecipe.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.caavo.myrecipe.R
import com.caavo.myrecipe.data.model.CartList
import com.caavo.myrecipe.data.model.RecipeDetails
import com.caavo.myrecipe.ui.RecipeDetailsImpl
import com.caavo.myrecipe.ui.recipeDetails.RecipeViewModel
import kotlinx.android.synthetic.main.recipe_view.view.*
import kotlinx.coroutines.*


class RecipeListAdapter(
    private val recipeDetailsImpl: RecipeDetailsImpl,
    private val recipeViewModel: RecipeViewModel,
) :
    RecyclerView.Adapter<RecipeListAdapter.TrendingRepoHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrendingRepoHolder {
        return TrendingRepoHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recipe_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TrendingRepoHolder, position: Int) {
        val currentList = differ.currentList[position]

        holder.itemView.apply {
            Glide.with(holder.itemView)
                .asBitmap()
                .load(currentList.image)
                .into(imageViewProduct)

            textViewProductName.text = currentList.name
            textViewPrice.text =   "RS. ${currentList.price}"
            val value = GlobalScope.launch(Dispatchers.IO) {
                var isInCart: CartList = recipeViewModel.getCartItemById(currentList.productId)

                if (isInCart!=null && isInCart.productId == currentList.productId) {
                    buttonAddToCart.text = resources.getString(R.string.text_in_cart)
                } else {
                    buttonAddToCart.text = resources.getString(R.string.text_add_to_cart)
                }
                buttonAddToCart.setOnClickListener {
                    if (isInCart == null || isInCart.productId != currentList.productId) {
                        buttonAddToCart.text = resources.getString(R.string.text_in_cart)
                        recipeDetailsImpl.buttonClickListenerAddToCart(currentList)
                    }

                }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class TrendingRepoHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<RecipeDetails>() {
        override fun areItemsTheSame(oldItem: RecipeDetails, newItem: RecipeDetails): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: RecipeDetails, newItem: RecipeDetails): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

}