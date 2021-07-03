package dev.fummicc1.lit.snacker.views

import androidx.recyclerview.widget.DiffUtil
import dev.fummicc1.lit.snacker.models.Identifiable

class RecyclerDiffCallback<Item: Identifiable>(private val old: List<Item>, private val new: List<Item>): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return old.size
    }

    override fun getNewListSize(): Int {
        return new.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return old[oldItemPosition] == new[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = old[oldItemPosition]
        val newItem = new[newItemPosition]

        return old == newItem
    }
}