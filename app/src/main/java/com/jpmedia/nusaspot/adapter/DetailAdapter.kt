package com.jpmedia.nusaspot.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jpmedia.nusaspot.R
import com.jpmedia.nusaspot.api.DetailData

class DetailAdapter(private val context: Context, private var detailDataList: MutableList<DetailData>) :
    RecyclerView.Adapter<DetailAdapter.DetailViewHolder>() {
    private var onDeleteButtonClickListener: ((detectId: String, id: Int) -> Unit)? = null

    fun setOnDeleteButtonClickListener(listener: (detectId: String, id: Int) -> Unit) {
        onDeleteButtonClickListener = listener
    }

    class DetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val detectImage: ImageView = itemView.findViewById(R.id.imageView)
        val description: TextView = itemView.findViewById(R.id.descriptionTextView)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
    }

    fun updateData(newData: List<DetailData>) {
        detailDataList.clear()
        detailDataList.addAll(newData)
        notifyDataSetChanged()
    }

    fun getDetailDataList(): MutableList<DetailData> {
        return detailDataList
    }

    fun removeAt(position: Int) {
        detailDataList.removeAt(position)
        notifyItemRemoved(position)
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

            holder.deleteButton.setOnClickListener {
                detailData.id?.let { it1 -> onDeleteButtonClickListener?.invoke(detailData.detectId.toString(), it1) }
            }
        }
    }

    override fun getItemCount(): Int {
        return detailDataList.size
    }
}
