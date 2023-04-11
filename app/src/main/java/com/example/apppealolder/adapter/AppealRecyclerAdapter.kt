package com.example.apppealolder.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.apppealolder.model.AppealInfo
import com.example.apppealolder.R

class AppealRecyclerAdapter : RecyclerView.Adapter<AppealRecyclerAdapter.ViewHolder>() {

    var onItemClick: ((AppealInfo) -> Unit)? = null

    private val diffItemCallback = object : DiffUtil.ItemCallback<AppealInfo>() {
        override fun areItemsTheSame(oldItem: AppealInfo, newItem: AppealInfo) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: AppealInfo, newItem: AppealInfo) =
            oldItem == newItem
    }

    val differ = AsyncListDiffer(this, diffItemCallback)


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bind(position: Int) {
            val data = differ.currentList[position]
            val phone: TextView = itemView.findViewById(R.id.phone_number_id)
            val district: TextView = itemView.findViewById(R.id.district_text)
            val requestDate: TextView = itemView.findViewById(R.id.date_id)
            val description: TextView = itemView.findViewById(R.id.desc_text)
            val detailButton: AppCompatButton = itemView.findViewById(R.id.detail_btn)
            val indexText: TextView = itemView.findViewById(R.id.appeal_index_id)
            val label: ImageView = itemView.findViewById(R.id.label_id)
            if (data.isAllow == 1) {
                label.setImageResource(R.drawable.bookmark)
            }
            indexText.text = "#".plus(data.id)
            phone.text = data.phone_number
            district.text = trimDistrict(data.district!!)
            requestDate.text = data.request_data
            description.text = trimDescription(data.description!!)
            detailButton.setOnClickListener {
                onItemClick?.invoke(data)
            }


        }

        private fun trimDescription(string: String): String {
            return string.substring(0, 16).plus("...")

        }

        private fun trimDistrict(string: String): String {
            return string.substring(0, 14).plus("...")
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.appeal_item, parent, false)
        return ViewHolder(view)

    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

}

