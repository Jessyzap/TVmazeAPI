package com.api.tvmaze.ui.fragments

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.api.tvmaze.databinding.FragmentEpisodeBinding
import com.api.tvmaze.model.Episode
import com.api.tvmaze.model.Season
import com.api.tvmaze.ui.adapter.EpisodeListAdapter
import com.api.tvmaze.viewModel.ShowViewModel


class EpisodeFragment : Fragment() {

    private lateinit var binding: FragmentEpisodeBinding
    private var model = ShowViewModel()


    private val rvEpisode by lazy {
        binding.rvEpisode
    }

    private val loading by lazy {
        binding.progressBarEpisodeList
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model = ViewModelProvider(requireActivity()).get(ShowViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentEpisodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        model.seasonLiveData.observe(viewLifecycleOwner, object : Observer<Season> {

            override fun onChanged(t: Season?) {
                t?.let {

                    EpisodeTask(t.id).execute()
                }
            }
        })

        rvEpisode.layoutManager = LinearLayoutManager(activity)
    }

    inner class EpisodeTask(id: Int) : AsyncTask<Void, Void, List<Episode>?>() {

        private var seasonID = id

        override fun doInBackground(vararg params: Void?): List<Episode>? {

            return model.callEpisode.getEpisodeAPI(seasonID).execute().body()
        }

        override fun onPostExecute(result: List<Episode>?) {
            super.onPostExecute(result)

            loading.visibility = View.GONE

            val episodeListAdapter = result?.let { EpisodeListAdapter(it, requireActivity()) }
            rvEpisode.adapter = episodeListAdapter
            episodeListAdapter?.notifyDataSetChanged()
        }
    }
}