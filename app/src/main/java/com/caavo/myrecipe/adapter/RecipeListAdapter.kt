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
import com.caavo.myrecipe.data.model.RecipeDetails
import kotlinx.android.synthetic.main.recipe_view.view.*
import java.util.*
import kotlin.collections.ArrayList


class RecipeListAdapter :
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
            textViewPrice.text = currentList.price
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class TrendingRepoHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    private val differCallback = object : DiffUtil.ItemCallback<RecipeDetails>() {
        override fun areItemsTheSame(oldItem: RecipeDetails, newItem: RecipeDetails): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RecipeDetails, newItem: RecipeDetails): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

}