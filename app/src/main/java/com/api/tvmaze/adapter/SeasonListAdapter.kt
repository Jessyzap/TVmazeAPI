package com.api.tvmaze.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.api.tvmaze.R
import com.api.tvmaze.fragments.EpisodeFragment
import com.api.tvmaze.model.Season
import com.api.tvmaze.model.Show
import com.api.tvmaze.viewModel.ShowViewModel

class SeasonListAdapter(
    private val seasonList: List<Season>,
    private val context: Context
) : RecyclerView.Adapter<SeasonListAdapter.SeasonListViewHolder>() {

    private var model: ShowViewModel? = null


    inner class SeasonListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val title: TextView = itemView.findViewById(R.id.seasonTitle)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SeasonListAdapter.SeasonListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_season_list, parent, false)
        return SeasonListViewHolder(view)
    }

    override fun getItemCount(): Int = seasonList.size

    override fun onBindViewHolder(holder: SeasonListAdapter.SeasonListViewHolder, position: Int) {


        holder.title.text = seasonList[position].seasonDetail()
        holder.itemView.setOnClickListener {

            model = ViewModelProvider(context as AppCompatActivity).get(ShowViewModel::class.java)

            model!!.responseSeason(
                Season(
                    seasonList[position].id,
                    seasonList[position].number
                )
            )

            val context = context as AppCompatActivity
            val transaction: FragmentTransaction = context.supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container_view, EpisodeFragment()).commit()
            transaction.addToBackStack("show detail")
        }
    }
}