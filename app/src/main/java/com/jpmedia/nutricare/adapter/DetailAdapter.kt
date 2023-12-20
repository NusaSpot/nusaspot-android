package com.jpmedia.nutricare.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.jpmedia.nutricare.R
import com.jpmedia.nutricare.api.DetailData




class DetailAdapter(private val context: Context, private var detailDataList: MutableList<DetailData>) :
    RecyclerView.Adapter<DetailAdapter.DetailViewHolder>() {
    private var onDeleteButtonClickListener: ((detectId: String, id: Int) -> Unit)? = null
    private var status: Int = 0

    fun setOnDeleteButtonClickListener(listener: (detectId: String, id: Int) -> Unit) {
        onDeleteButtonClickListener = listener
    }

    fun updateData(newData: List<DetailData>, status: Int) {
        this.status = status
        detailDataList.clear()
        detailDataList.addAll(newData)
        notifyDataSetChanged()
    }

    fun getDetailDataList(): List<DetailData> {
        return detailDataList
    }

    fun removeAt(position: Int) {
        detailDataList.removeAt(position)
        notifyItemRemoved(position)
    }

    class DetailViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val detectImage: ImageView = itemView.findViewById(R.id.imageView)
        val description: TextView = itemView.findViewById(R.id.descriptionTextView)
        val deleteButton: ImageView = itemView.findViewById(R.id.deleteButton)
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
            holder.description.text = "${detailData.result}"

            // Cek status di sini
            if (status == 1) {
                // Status = 1, sembunyikan tombol delete
                holder.deleteButton.visibility = View.GONE
            } else {
                // Status bukan 1, tampilkan tombol delete
                holder.deleteButton.visibility = View.VISIBLE

                holder.deleteButton.setOnClickListener {
                    detailData.id?.let { it1 ->
                        onDeleteButtonClickListener?.invoke(
                            detailData.detectId.toString(),
                            it1
                        )
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return detailDataList.size
    }
}
