package com.jpmedia.nutricare.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jpmedia.nutricare.api.NutritionistData
import com.jpmedia.nutricare.databinding.ItemAhliGiziBinding

class NutritionistAdapter(private val clickListener: OnNutritionistClickListener) : RecyclerView.Adapter<NutritionistAdapter.NutritionistViewHolder>() {

    private val nutritionists = mutableListOf<NutritionistData>()

    fun setNutritionists(newNutritionists: List<NutritionistData>) {
        nutritionists.clear()
        nutritionists.addAll(newNutritionists)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NutritionistViewHolder {
        val binding = ItemAhliGiziBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NutritionistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NutritionistViewHolder, position: Int) {
        holder.bind(nutritionists[position])
    }

    override fun getItemCount(): Int = nutritionists.size

    inner class NutritionistViewHolder(private val binding: ItemAhliGiziBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(nutritionistData: NutritionistData) {

            val nutritionistProfile = nutritionistData.nutritionist_profile
            binding.textView.text = nutritionistData.name
            //binding.chat.text = nutritionistProfile.phone
            Glide.with(binding.root.context)
                .load(nutritionistProfile?.profile_picture)
                .into(binding.imageView)

            // Set click listener
            binding.root.setOnClickListener {

                nutritionistProfile?.phone?.let { it1 -> clickListener.onNutritionistClick(it1) }

            }
        }
    }

    interface OnNutritionistClickListener {
        fun onNutritionistClick(nutritionistData: String)
    }
}
