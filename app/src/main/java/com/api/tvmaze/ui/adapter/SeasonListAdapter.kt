package com.api.tvmaze.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.api.tvmaze.R
import com.api.tvmaze.databinding.ItemSeasonListBinding
import com.api.tvmaze.model.Season
import com.api.tvmaze.viewModel.ShowViewModel

class SeasonListAdapter(
    private val seasonList: List<Season>,
    private val context: Context) :
    RecyclerView.Adapter<SeasonListAdapter.SeasonListViewHolder>() {


    private var model: ShowViewModel? = null


    inner class SeasonListViewHolder(binding: ItemSeasonListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val title = binding.seasonTitle
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeasonListViewHolder {

        val binding =
            ItemSeasonListBinding.inflate(LayoutInflater.from(context), parent,false)

        return SeasonListViewHolder(binding)
    }


    override fun getItemCount(): Int = seasonList.size


    override fun onBindViewHolder(holder: SeasonListAdapter.SeasonListViewHolder, position: Int) {

        holder.title.text = seasonList[position].seasonDetail()
        holder.itemView.setOnClickListener {

            model = ViewModelProvider(context as AppCompatActivity).get(ShowViewModel::class.java)

            model?.responseSeason(
                Season(
                    seasonList[position].id,
                    seasonList[position].number
                )
            )

            it.findNavController().navigate(R.id.action_showDetailFragment_to_episodeFragment)
        }
    }
}