package com.jpmedia.nusaspot.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jpmedia.nusaspot.R
import com.jpmedia.nusaspot.api.Recipe

class ResepAdapter : RecyclerView.Adapter<ResepAdapter.RecipeViewHolder>() {
    private var recipes: List<Recipe> = emptyList()
    private var itemClickListener: OnItemClickListener? = null
    interface OnItemClickListener {
        fun onItemClick(recipe: Recipe)
    }
    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_resep, parent, false)
        return RecipeViewHolder(view)
    }
    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.bind(recipe)
    }
    override fun getItemCount(): Int {
        return recipes.size
    }
    fun setData(data: List<Recipe>) {
        recipes = data
        notifyDataSetChanged()
    }
    inner class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textTitle: TextView = itemView.findViewById(R.id.title)
        private val imageView: ImageView = itemView.findViewById(R.id.img_resep)
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener?.onItemClick(recipes[position])
                }
            }
        }
        fun bind(recipe: Recipe) {
            textTitle.text = recipe.title
            Glide.with(itemView.context)
                .load(recipe.image)
                .into(imageView)
        }
    }
}