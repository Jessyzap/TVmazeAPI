package com.api.tvmaze.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.api.tvmaze.R
import com.api.tvmaze.databinding.ItemHomeListBinding
import com.api.tvmaze.model.Search
import com.api.tvmaze.model.Show
import com.api.tvmaze.viewModel.ShowViewModel

class SearchListAdapter(private val searchList: List<Search>, private val context: Context) :
    RecyclerView.Adapter<SearchListAdapter.HomeListViewHolder>() {


    private var model: ShowViewModel? = null


    inner class HomeListViewHolder(binding: ItemHomeListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val title = binding.movieTitle
        val image = binding.movieImage
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeListViewHolder {

        val binding =
            ItemHomeListBinding.inflate(LayoutInflater.from(context), parent,false)

        return HomeListViewHolder(binding)
    }


    override fun getItemCount(): Int = searchList.size


    override fun onBindViewHolder(holder: HomeListViewHolder, position: Int) {


        holder.title.text = searchList[position].show.name
        searchList[position].show.image?.let{ holder.image.load(searchList[position].show.image?.medium) }


        holder.itemView.setOnClickListener {

            model = ViewModelProvider(context as AppCompatActivity).get(ShowViewModel::class.java)


            model?.response(
                Show(
                    searchList[position].show.id,
                    searchList[position].show.genres,
                    searchList[position].show.schedule,
                    searchList[position].show.image,
                    searchList[position].show.name,
                    searchList[position].show.summary
                )
            )

            it.findNavController().navigate(R.id.action_homeFragment_to_showDetailFragment)
        }
    }
}
