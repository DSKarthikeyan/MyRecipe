package com.caavo.myrecipe.adapter

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.caavo.myrecipe.R
import com.caavo.myrecipe.data.model.CartList
import com.caavo.myrecipe.data.model.RecipeDetails
import com.caavo.myrecipe.ui.RecipeDetailsImpl
import kotlinx.android.synthetic.main.recipe_view.view.*
import kotlinx.coroutines.coroutineScope
import java.util.*
import kotlin.collections.ArrayList


class CartListAdapter(private val recipeDetailsImpl: RecipeDetailsImpl) :
    RecyclerView.Adapter<CartListAdapter.CartListHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartListHolder {
        return CartListHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recipe_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CartListHolder, position: Int) {
        val currentList = differ.currentList[position]

        holder.itemView.apply {
            Glide.with(holder.itemView)
                .asBitmap()
                .load(currentList.image)
                .into(imageViewProduct)

            textViewProductName.text = currentList.name
            textViewPrice.text = "RS. ${currentList.price}"

            buttonAddToCart.text = resources.getString(R.string.text_remove_from_cart)
            buttonAddToCart.setOnClickListener {
                recipeDetailsImpl.buttonClickListenerRemoveFromCart(currentList)
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class CartListHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<CartList>() {
        override fun areItemsTheSame(oldItem: CartList, newItem: CartList): Boolean {
            return oldItem.productId == newItem.productId
        }

        override fun areContentsTheSame(oldItem: CartList, newItem: CartList): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

}