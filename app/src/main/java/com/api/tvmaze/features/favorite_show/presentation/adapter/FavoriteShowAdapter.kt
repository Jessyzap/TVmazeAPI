package com.api.tvmaze.features.favorite_show.presentation.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import coil.load

import com.api.tvmaze.databinding.ItemFavoriteShowListBinding
import com.api.tvmaze.features.favorite_show.data.model.ShowObject
import com.api.tvmaze.utils.ShowDiffUtilCallback

class FavoriteShowAdapter(
    private val context: Context,
    private val callback: (ShowObject) -> Unit,
    private val menuCallback: (ShowObject, View) -> Unit
) : RecyclerView.Adapter<FavoriteShowAdapter.FavoriteShowListViewHolder>() {

    private var favoriteShowList: List<ShowObject> = emptyList()

    inner class FavoriteShowListViewHolder(val binding: ItemFavoriteShowListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(show: ShowObject) {

            binding.apply {
                txtGenre.text = show.genres.joinToString(separator = ", ")
                imgShow.load(show.image?.medium)
                txtSchedule.text = show.schedule?.scheduleDetail()
                txtTitle.text = show.name
            }

            binding.imgMenu.setOnClickListener {
                menuCallback.invoke(show, binding.imgMenu)
            }

            itemView.setOnClickListener {
                callback.invoke(show)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): FavoriteShowListViewHolder {

        val binding =
            ItemFavoriteShowListBinding.inflate(LayoutInflater.from(context), parent, false)

        return FavoriteShowListViewHolder(binding)
    }


    override fun getItemCount(): Int = favoriteShowList.size


    override fun onBindViewHolder(
        holder: FavoriteShowListViewHolder,
        position: Int
    ) {
        holder.bind(favoriteShowList[position])
    }

    fun updateList(newList: List<ShowObject>) {
        val diffResult = DiffUtil.calculateDiff(ShowDiffUtilCallback(favoriteShowList, newList))
        favoriteShowList = newList
        diffResult.dispatchUpdatesTo(this)
    }

}