package com.api.tvmaze.features.show.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.api.tvmaze.R
import com.api.tvmaze.databinding.FragmentEpisodeBinding
import com.api.tvmaze.features.show.presentation.adapter.EpisodeListAdapter
import com.api.tvmaze.features.show.presentation.viewModel.ShowViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowEpisodeListFragment : Fragment() {

    private lateinit var binding: FragmentEpisodeBinding
    private lateinit var model: ShowViewModel
    private lateinit var adapter: EpisodeListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model = ViewModelProvider(requireActivity())[ShowViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentEpisodeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()

        model.seasonLiveData.observe(viewLifecycleOwner) { season ->
            season?.let {
                model.getEpisodes(season.id)
            }
        }

        model.episodeLiveDataList.observe(viewLifecycleOwner) { episodeList ->
            binding.progressBarEpisodeList.visibility = View.GONE
            adapter.updateList(episodeList)
        }
    }

    private fun setupAdapter() {
        adapter = EpisodeListAdapter(requireActivity()) { episode ->
            findNavController().navigate(R.id.action_episodeFragment_to_episodeDetailFragment)
            model.setEpisode(episode)
        }
        binding.rvEpisode.adapter = adapter
    }


}