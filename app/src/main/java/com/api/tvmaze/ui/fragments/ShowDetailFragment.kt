package com.api.tvmaze.ui.fragments

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.parseAsHtml
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.api.tvmaze.databinding.FragmentShowDetailBinding
import com.api.tvmaze.model.Season
import com.api.tvmaze.model.Show
import com.api.tvmaze.ui.adapter.SeasonListAdapter
import com.api.tvmaze.viewModel.ShowViewModel


class ShowDetailFragment : Fragment() {

    private lateinit var binding: FragmentShowDetailBinding
    private var model = ShowViewModel()


    private val rvSeason by lazy {
        binding.rvSeason
    }

    private val loading by lazy {
        binding.progressBarShowDetail
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model = ViewModelProvider(requireActivity()).get(ShowViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentShowDetailBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        val showTitle = binding.txtTitle
        val showImage = binding.imgShow
        val showGenre = binding.txtGenre
        val showSchedule = binding.txtSchedule
        val showDescription = binding.txtDescription


        model.showLiveData.observe(viewLifecycleOwner, object : Observer<Show> {

            override fun onChanged(t: Show?) {
                t?.let {

                    showGenre.text = t.genre.joinToString(separator = ", ")
                    showImage.load(t.image?.medium)
                    showSchedule.text = t.schedule.scheduleDetail()
                    showTitle.text = t.title
                    showDescription.text = t.description.parseAsHtml()

                    SeasonTask(t.id).execute()
                }
            }
        })

        rvSeason.layoutManager = LinearLayoutManager(activity)
    }

    inner class SeasonTask(id: Int) : AsyncTask<Void, Void, List<Season>?>() {

        private var showID = id
        private val seasonBar = binding.seasonBar


        override fun doInBackground(vararg params: Void?): List<Season>? {

            return model.callSeason.getSeasonAPI(showID).execute().body()
        }

        override fun onPostExecute(result: List<Season>?) {
            super.onPostExecute(result)

            loading.visibility = View.GONE

            result?.let { if (it.size > 1 ) {
                seasonBar.text = "${it.size} Seasons"
            } }

            val seasonListAdapter = result?.let { SeasonListAdapter(it, requireActivity()) }
            rvSeason.adapter = seasonListAdapter
            seasonListAdapter?.notifyDataSetChanged()
        }
    }
}