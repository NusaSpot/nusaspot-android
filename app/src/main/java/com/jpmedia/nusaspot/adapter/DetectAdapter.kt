package com.jpmedia.nusaspot.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jpmedia.nusaspot.R
import com.jpmedia.nusaspot.api.DetectDataItem

class DetectAdapter(private var detectList: List<DetectDataItem>,  private val context: Context) : RecyclerView.Adapter<DetectAdapter.DetectViewHolder>() {

    private var onItemClickListener: ((Int) -> Unit)? = null

    class DetectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val idTextView: TextView = itemView.findViewById(R.id.id)
        val userIdTextView: TextView = itemView.findViewById(R.id.user_id)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetectViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_detect, parent, false)
        return DetectViewHolder(view)
    }

    fun updateData(newDetectList: List<DetectDataItem>) {
        detectList = newDetectList
        notifyDataSetChanged()
    }

    fun setOnItemClickListener(listener: (Int) -> Unit) {
        onItemClickListener = listener
    }

    override fun onBindViewHolder(holder: DetectViewHolder, position: Int) {
        val currentItem = detectList[position]
        holder.idTextView.text = "ID: ${currentItem.id?: "Unknown"}"
        holder.userIdTextView.text = "User ID: ${currentItem.userId}"

        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(position)
        }
    }


    override fun getItemCount(): Int {
        return detectList.size
    }

    fun getItem(position: Int): DetectDataItem {
        return detectList[position]
    }
}

