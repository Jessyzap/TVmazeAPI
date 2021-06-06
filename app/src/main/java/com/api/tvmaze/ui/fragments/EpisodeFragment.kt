package com.api.tvmaze.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.api.tvmaze.R
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
    ): View {

        binding = FragmentEpisodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        model.seasonLiveData.observe(viewLifecycleOwner,
            Observer<Season> { season ->
                season?.let {
                    model.getEpisodes(season.id)
                }
            })

        model.episodeLiveDataList.observe(viewLifecycleOwner,
            Observer<List<Episode>> { episodeList ->
                loading.visibility = View.GONE

                val episodeListAdapter =
                    episodeList?.let { EpisodeListAdapter(episodeList, requireActivity()) }
                rvEpisode.adapter = episodeListAdapter
                episodeListAdapter?.notifyDataSetChanged()
            })
    }

    override fun onResume() {
        super.onResume()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigate(R.id.showDetailFragment)
        }
    }
}