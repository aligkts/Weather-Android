package com.aligkts.weatherapp.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.aligkts.weatherapp.network.response.WeatherByLocationResponse

class FavoritesAdapter(var itemList: List<WeatherByLocationResponse>) : RecyclerView.Adapter<FavoritesViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {

        return FavoritesViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }


    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        holder.bindTo(holder.itemView.context, itemList[position])
    }

    fun setNewList(itemList: List<WeatherByLocationResponse>) {

        this.itemList = itemList
        notifyDataSetChanged()
    }
}