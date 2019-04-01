package com.aligkts.weatherapp.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aligkts.weatherapp.network.response.WeatherByLocationResponse

class DetailAdapter(var itemList: List<WeatherByLocationResponse>) : RecyclerView.Adapter<DetailViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {

        return DetailViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }


    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        holder.bindTo(holder.itemView.context, itemList[position])
    }

    fun setNewList(itemList: List<WeatherByLocationResponse>) {

        this.itemList = itemList
        notifyDataSetChanged()
    }
}