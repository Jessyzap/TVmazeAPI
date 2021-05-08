package com.api.tvmaze.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.api.tvmaze.R
import com.api.tvmaze.databinding.ItemEpisodeListBinding
import com.api.tvmaze.model.Episode
import com.api.tvmaze.ui.fragments.EpisodeDetailFragment
import com.api.tvmaze.viewModel.ShowViewModel

class EpisodeListAdapter(
    private val episodeList: List<Episode>,
    private val context: Context) :
    RecyclerView.Adapter<EpisodeListAdapter.EpisodeListViewHolder>() {

    private var model: ShowViewModel? = null


    inner class EpisodeListViewHolder(binding: ItemEpisodeListBinding) :
        RecyclerView.ViewHolder(binding.root) {


        val title = binding.episodeTitle
        val image = binding.episodeImage
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int
    ): EpisodeListAdapter.EpisodeListViewHolder {

        val binding =
            ItemEpisodeListBinding.inflate(LayoutInflater.from(context), parent,false)

        return EpisodeListViewHolder(binding)
    }


    override fun getItemCount(): Int = episodeList.size


    override fun onBindViewHolder(holder: EpisodeListAdapter.EpisodeListViewHolder, position: Int) {

        holder.title.text = episodeList[position].seasonEpisode()
        episodeList[position].image?.let{ holder.image.load(episodeList[position].image?.original)}


        holder.itemView.setOnClickListener {

            model = ViewModelProvider(context as AppCompatActivity).get(ShowViewModel::class.java)

            model?.responseEpisode(
                Episode(
                    episodeList[position].id,
                    episodeList[position].title,
                    episodeList[position].image,
                    episodeList[position].season,
                    episodeList[position].episode,
                    episodeList[position].description
                )
            )

            val context = context as AppCompatActivity
            val transaction: FragmentTransaction = context.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container_view, EpisodeDetailFragment()).commit()
            transaction.addToBackStack("list of episodes")
        }
    }
}