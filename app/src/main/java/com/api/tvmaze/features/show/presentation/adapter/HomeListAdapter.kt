package com.api.tvmaze.features.show.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.api.tvmaze.databinding.ItemHomeListBinding
import com.api.tvmaze.features.show.domain.entity.ShowEntity

class HomeListAdapter(
    private val context: Context,
    private var callback: (ShowEntity) -> Unit
) : PagingDataAdapter<ShowEntity, HomeListAdapter.HomeListViewHolder>(ShowDiffCallback) {

    inner class HomeListViewHolder(private val binding: ItemHomeListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(show: ShowEntity) {
            binding.movieTitle.text = show.name
            show.image?.medium?.let { binding.movieImage.load(it) }

            itemView.setOnClickListener {
                callback.invoke(show)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeListViewHolder {
        val binding =
            ItemHomeListBinding.inflate(LayoutInflater.from(context), parent, false)

        return HomeListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeListViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        private val ShowDiffCallback = object : DiffUtil.ItemCallback<ShowEntity>() {
            override fun areItemsTheSame(oldItem: ShowEntity, newItem: ShowEntity): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ShowEntity, newItem: ShowEntity): Boolean =
                oldItem == newItem
        }
    }

}