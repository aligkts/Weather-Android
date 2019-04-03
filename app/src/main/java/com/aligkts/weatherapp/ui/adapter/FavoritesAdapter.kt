package com.aligkts.weatherapp.ui.adapter

import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.aligkts.weatherapp.util.INotifyRecycler
import com.aligkts.weatherapp.network.response.WeatherByLocationResponse

class FavoritesAdapter(var itemList: List<WeatherByLocationResponse>,
                       var listener: INotifyRecycler) : RecyclerView.Adapter<FavoritesViewHolder>(),Filterable {

    lateinit var searchedListFiltered: List<WeatherByLocationResponse>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        return FavoritesViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return searchedListFiltered.size
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        holder.bindTo(holder.itemView.context,searchedListFiltered[position],listener)
        holder.itemView.isLongClickable = true
    }

    fun setNewList(itemList: List<WeatherByLocationResponse>) {
        this.itemList = itemList
        this.searchedListFiltered = itemList
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    searchedListFiltered = itemList
                } else {
                    val filteredList = ArrayList<WeatherByLocationResponse>()
                    for (row in itemList) {
                        if (row.name?.toLowerCase()!!.contains(charString.toLowerCase())) {
                            filteredList.add(row)
                        }
                    }
                    searchedListFiltered = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = searchedListFiltered
                return filterResults
            }
            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults?) {
                filterResults?.let {
                    searchedListFiltered = it.values as ArrayList<WeatherByLocationResponse>
                    notifyDataSetChanged()
                }
            }
        }
    }
}