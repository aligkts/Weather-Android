package com.aligkts.weatherapp.view.ui.adapter

import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.aligkts.weatherapp.data.network.model.ModelResponse
import com.aligkts.weatherapp.data.INotifyRecycler
import java.lang.IllegalArgumentException

class FavoritesAdapter(private var itemList: List<ModelResponse>,
                       private var listener: INotifyRecycler) : RecyclerView.Adapter<FavoritesViewHolder>(), Filterable  {

    lateinit var dataListUnFilter: ArrayList<ModelResponse>

    init {
        setHasStableIds(true)
    }

    @Throws(IllegalArgumentException::class)
    override fun setHasStableIds(hasStableIds: Boolean) {
        if (!hasStableIds) {
            throw IllegalArgumentException("StableListItemAdapter does not allow unstable ids")
        }
        super.setHasStableIds(hasStableIds)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesViewHolder {
        return FavoritesViewHolder(parent)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: FavoritesViewHolder, position: Int) {
        holder.bindTo(holder.itemView.context, itemList[position], listener)
        holder.itemView.isLongClickable = true
    }

    override fun getItemId(position: Int): Long {
        return itemList[position].id.hashCode().toLong()
    }

    fun setNewList(itemList: List<ModelResponse>) {
        this.itemList = itemList
        this.dataListUnFilter = itemList as ArrayList<ModelResponse>
        notifyDataSetChanged()
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence?): FilterResults {
                val charString = charSequence.toString().trim()
                itemList = if (charString.isEmpty()) {
                    dataListUnFilter
                } else {
                    val filteredList = ArrayList<ModelResponse>()
                    for (row in dataListUnFilter) {
                        if (row.name?.toLowerCase()!!.contains(charString.toLowerCase())) {
                            filteredList.add(row)
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = itemList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence?, filterResults: FilterResults?) {
                filterResults?.let { _filterResult->
                    _filterResult.values?.let {
                        itemList = it as ArrayList<ModelResponse>
                        notifyDataSetChanged()
                    }
                }
            }
        }
    }

}