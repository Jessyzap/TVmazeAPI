package com.api.tvmaze.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.api.tvmaze.databinding.ItemSeasonListBinding
import com.api.tvmaze.data.model.Season
import com.api.tvmaze.utils.ShowDiffUtilCallback

class SeasonListAdapter(
    private val context: Context,
    private val callback: (Season) -> Unit
) : RecyclerView.Adapter<SeasonListAdapter.SeasonListViewHolder>() {

    private var seasonList: List<Season> = emptyList()

    inner class SeasonListViewHolder(binding: ItemSeasonListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val title = binding.seasonTitle

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeasonListViewHolder {
        val binding = ItemSeasonListBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false
        )
        return SeasonListViewHolder(binding)
    }

    override fun getItemCount(): Int = seasonList.size

    override fun onBindViewHolder(holder: SeasonListViewHolder, position: Int) {
        holder.title.text = seasonList[position].seasonDetail()

        holder.itemView.setOnClickListener {
            callback.invoke(seasonList[position])
        }

    }

    fun updateList(newList: List<Season>) {
        val diffResult = DiffUtil.calculateDiff(ShowDiffUtilCallback(seasonList, newList))
        seasonList = newList
        diffResult.dispatchUpdatesTo(this)
    }
}