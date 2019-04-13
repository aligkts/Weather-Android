package com.aligkts.weatherapp.view.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aligkts.weatherapp.data.network.model.ModelResponse

class DetailAdapter(var itemList: List<ModelResponse>) : RecyclerView.Adapter<DetailViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        return DetailViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        holder.bindTo(holder.itemView.context, itemList[position])
    }

    fun setNewList(itemList: List<ModelResponse>) {
        this.itemList = itemList
        notifyDataSetChanged()
    }
}