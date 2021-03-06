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
import com.api.tvmaze.model.Show
import com.api.tvmaze.viewModel.ShowViewModel

class HomeListAdapter(
    private var showList: List<Show>,
    private val context: Context
) :
    RecyclerView.Adapter<HomeListAdapter.HomeListViewHolder>() {


    private var model: ShowViewModel? = null


    inner class HomeListViewHolder(binding: ItemHomeListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val title = binding.movieTitle
        val image = binding.movieImage
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeListViewHolder {

        val binding =
            ItemHomeListBinding.inflate(LayoutInflater.from(context), parent, false)

        return HomeListViewHolder(binding)
    }


    override fun getItemCount(): Int = showList.size


    override fun onBindViewHolder(holder: HomeListViewHolder, position: Int) {

        holder.title.text = showList[position].name

        //Glide.with(context).load(showList[position].image).into(holder.image.medium)
        showList[position].image?.let { holder.image.load(showList[position].image?.medium) }

        holder.itemView.setOnClickListener {

            model = ViewModelProvider(context as AppCompatActivity).get(ShowViewModel::class.java)

            model?.response(
                Show(
                    showList[position].id,
                    showList[position].genres,
                    showList[position].schedule,
                    showList[position].image,
                    showList[position].name,
                    showList[position].summary
                )
            )
            it.findNavController().navigate(R.id.action_homeFragment_to_showDetailFragment)
        }
    }
}
