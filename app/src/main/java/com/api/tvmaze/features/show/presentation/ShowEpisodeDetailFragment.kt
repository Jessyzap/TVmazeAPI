package com.api.tvmaze.features.show.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.parseAsHtml
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.api.tvmaze.databinding.FragmentEpisodeDetailBinding
import com.api.tvmaze.features.show.presentation.viewModel.ShowViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowEpisodeDetailFragment : Fragment() {

    private lateinit var binding: FragmentEpisodeDetailBinding
    private lateinit var model: ShowViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model = ViewModelProvider(requireActivity())[ShowViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentEpisodeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model.episodeLiveData.observe(viewLifecycleOwner) { episode ->

            episode?.let {
                binding.apply {
                    txtEpisodeTitle.text = episode.name
                    episode.image?.original?.let { imgEpisode.load(it) }
                    txtSeasonEpisode.text = episode.seasonComplete
                    txtEpisode.text = episode.episodeComplete
                    txtEpisodeDescription.text = episode.summary?.parseAsHtml()
                }
            }
        }
    }

}