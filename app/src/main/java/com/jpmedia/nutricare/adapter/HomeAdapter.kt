package com.jpmedia.nutricare.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jpmedia.nutricare.api.Recipe
import com.jpmedia.nutricare.databinding.ItemHomeBinding



class HomeAdapter(private val clickListener: OnRecipeClickListener) : RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    private val recipes = mutableListOf<Recipe>()
    fun setRecipes(newRecipes: List<Recipe>) {
        recipes.clear()
        recipes.addAll(newRecipes)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val binding = ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val viewHolder = HomeViewHolder(binding)

        binding.root.setOnClickListener {
            val position = viewHolder.adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val clickedRecipe = recipes[position]
                clickListener.onRecipeClick(clickedRecipe)
            }
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.bind(recipes[position])
    }

    override fun getItemCount(): Int = recipes.size

    class HomeViewHolder(private val binding: ItemHomeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Recipe) {
            binding.textTitle.text = recipe.title
            binding.description.text = recipe.category
            Glide.with(binding.root.context)
                .load(recipe.image)
                .into(binding.imageResep)
        }
    }

    interface OnRecipeClickListener {
        fun onRecipeClick(recipe: Recipe)
    }
}
