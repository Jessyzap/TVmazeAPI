package com.api.tvmaze.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.api.tvmaze.databinding.ItemHomeListBinding
import com.api.tvmaze.model.Show
import com.api.tvmaze.utils.ShowDiffUtilCallback

class HomeListAdapter(
    private val context: Context,
    private var callback: (Show) -> Unit
) : RecyclerView.Adapter<HomeListAdapter.HomeListViewHolder>() {

    private var showList: List<Show> = emptyList()

    inner class HomeListViewHolder(binding: ItemHomeListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val title = binding.movieTitle
        private val image = binding.movieImage

        fun bind(show: Show) {
            title.text = show.name
            show.image?.medium?.let { image.load(it) }

            itemView.setOnClickListener {
                callback.invoke(
                    Show(
                        show.id,
                        show.genres,
                        show.schedule,
                        show.image,
                        show.name,
                        show.summary
                    )
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeListViewHolder {
        val binding =
            ItemHomeListBinding.inflate(LayoutInflater.from(context), parent, false)

        return HomeListViewHolder(binding)
    }

    override fun getItemCount(): Int = showList.size

    override fun onBindViewHolder(holder: HomeListViewHolder, position: Int) {
        holder.bind(showList[position])
    }

    fun updateList(newList: List<Show>) {
        val diffResult = DiffUtil.calculateDiff(ShowDiffUtilCallback(showList, newList))
        showList = newList
        diffResult.dispatchUpdatesTo(this)
    }

}