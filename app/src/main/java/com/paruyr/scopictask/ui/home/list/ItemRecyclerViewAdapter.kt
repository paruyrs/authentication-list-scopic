package com.paruyr.scopictask.ui.home.list

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.paruyr.scopictask.data.model.list.ItemData

interface ItemClickListener {
    fun onItemRemove(item: ItemData)
}

class ItemRecyclerViewAdapter(
    private var values: MutableList<ItemData>?,
    private val itemClickListener: ItemClickListener
) : ListAdapter<ItemData, RecyclerView.ViewHolder>(ItemDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false)
        return object : RecyclerView.ViewHolder(itemView) {}
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        values?.getOrNull(position)?.let { item ->
            // Bind data to views within the default ViewHolder's itemView
            holder.itemView.findViewById<TextView>(android.R.id.text1).text = item.value
        }
    }

    fun updateItems(newList: MutableList<ItemData>?) {
        values = newList // Update the list in the adapter
        submitList(values)
    }

    fun removeItem(position: Int) {
        values?.let {
            val removedIdem = it.removeAt(position)
            notifyItemRemoved(position)
            itemClickListener.onItemRemove(removedIdem)
        }
    }
}

class ItemDiffCallback : DiffUtil.ItemCallback<ItemData>() {
    override fun areItemsTheSame(oldItem: ItemData, newItem: ItemData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: ItemData, newItem: ItemData): Boolean {
        return oldItem == newItem
    }
}