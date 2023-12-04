package com.jpmedia.nusaspot.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.jpmedia.nusaspot.R
import com.jpmedia.nusaspot.api.DetailData

class DetailAdapter(private val context: Context, private var detailDataList: List<DetailData>) :
    RecyclerView.Adapter<DetailAdapter.DetailViewHolder>() {

    class DetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Inisialisasi tampilan di sini
        val detectImage: ImageView = itemView.findViewById(R.id.imageView)
        val description: TextView = itemView.findViewById(R.id.descriptionTextView)

        // Tambahkan tampilan lain sesuai kebutuhan
    }

    // Deklarasikan fungsi updateData sebagai bagian dari kelas DetailAdapter
    fun updateData(newData: List<DetailData>) {
        detailDataList = newData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_detail, parent, false)
        return DetailViewHolder(view)
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        val detailData = detailDataList[position]
        detailData?.let {
            Glide.with(holder.detectImage.context)
                .load(it.image)
                .into(holder.detectImage)
            holder.description.text = "Deskripsi: ${detailData.result}"
        }
    }

    override fun getItemCount(): Int {
        return detailDataList.size
    }
}

