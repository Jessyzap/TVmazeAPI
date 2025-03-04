package com.api.tvmaze.utils

import androidx.recyclerview.widget.DiffUtil

class ShowDiffUtilCallback<T : DiffIdentifiable>(
    private val oldList: List<T>,
    private val newList: List<T>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].diffId == newList[newItemPosition].diffId
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}

interface DiffIdentifiable {
    val diffId: Int
}