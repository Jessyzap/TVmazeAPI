package com.api.tvmaze.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.api.tvmaze.databinding.ItemEpisodeListBinding
import com.api.tvmaze.model.Episode
import com.api.tvmaze.utils.ShowDiffUtilCallback

class EpisodeListAdapter(
    private val context: Context,
    private val callback: (Episode) -> Unit
) : RecyclerView.Adapter<EpisodeListAdapter.EpisodeListViewHolder>() {

    private var episodeList: List<Episode> = emptyList()

    inner class EpisodeListViewHolder(binding: ItemEpisodeListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val title = binding.episodeTitle
        val image = binding.episodeImage
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): EpisodeListAdapter.EpisodeListViewHolder {

        val binding =
            ItemEpisodeListBinding.inflate(LayoutInflater.from(context), parent, false)

        return EpisodeListViewHolder(binding)
    }


    override fun getItemCount(): Int = episodeList.size


    override fun onBindViewHolder(holder: EpisodeListAdapter.EpisodeListViewHolder, position: Int) {

        holder.title.text = episodeList[position].seasonEpisode()
        episodeList[position].image?.original?.let { holder.image.load(it) }


        holder.itemView.setOnClickListener {
            callback.invoke(
                Episode(
                    episodeList[position].id,
                    episodeList[position].name,
                    episodeList[position].image,
                    episodeList[position].season,
                    episodeList[position].number,
                    episodeList[position].summary
                )
            )
        }
    }

    fun updateList(newList: List<Episode>) {
        val diffResult = DiffUtil.calculateDiff(ShowDiffUtilCallback(episodeList, newList))
        episodeList = newList
        diffResult.dispatchUpdatesTo(this)
    }
}