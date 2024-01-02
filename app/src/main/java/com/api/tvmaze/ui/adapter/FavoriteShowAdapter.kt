package com.api.tvmaze.ui.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import coil.load

import com.api.tvmaze.databinding.ItemFavoriteShowListBinding
import com.api.tvmaze.model.ImageType
import com.api.tvmaze.model.ScheduleType
import com.api.tvmaze.model.Show
import com.api.tvmaze.ui.data.ShowObject
import com.api.tvmaze.utils.ShowDiffUtilCallback

class FavoriteShowAdapter(
    private val context: Context,
    private val callback: (Show) -> Unit,
    private val menuCallback: (Show, View) -> Unit
) : RecyclerView.Adapter<FavoriteShowAdapter.FavoriteShowListViewHolder>() {

    private var favoriteShowList: List<ShowObject> = emptyList()

    inner class FavoriteShowListViewHolder(val binding: ItemFavoriteShowListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(show: ShowObject) {

            val showEntity = Show(
                show.id                ,
                show.genres,
                ScheduleType(
                    time = show.schedule?.time.orEmpty(),
                    days = show.schedule?.days ?: listOf()
                ),
                ImageType(
                    medium = show.image?.medium,
                    original = show.image?.original
                ),
                show.name,
                show.summary
            )

            binding.apply {
                txtGenre.text = show.genres.joinToString(separator = ", ")
                imgShow.load(show.image?.medium)
                txtSchedule.text = show.schedule?.scheduleDetail()
                txtTitle.text = show.name
            }

            binding.imgMenu.setOnClickListener {
                menuCallback.invoke(showEntity,binding.imgMenu)
            }

            itemView.setOnClickListener {
                callback.invoke(showEntity)
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