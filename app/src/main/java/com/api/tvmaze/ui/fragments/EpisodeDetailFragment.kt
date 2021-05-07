package com.api.tvmaze.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.parseAsHtml
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.api.tvmaze.databinding.FragmentEpisodeDetailBinding
import com.api.tvmaze.model.Episode
import com.api.tvmaze.viewModel.ShowViewModel


class EpisodeDetailFragment : Fragment() {

    private var _binding: FragmentEpisodeDetailBinding? = null
    private val binding: FragmentEpisodeDetailBinding get() = _binding!!
    private var model = ShowViewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model = ViewModelProvider(requireActivity()).get(ShowViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentEpisodeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        val episodeTitle = binding.txtEpisodeTitle
        val episodeImage = binding.imgEpisode
        val episodeSeason = binding.txtSeasonEpisode
        val episodeNumber = binding.txtEpisode
        val episodeDescription = binding.txtEpisodeDescription


        model.episodeLiveData.observe(viewLifecycleOwner, object : Observer<Episode> {

            override fun onChanged(t: Episode?) {
                t?.let {

                    episodeTitle.text = t.title
                    episodeImage.load(t.image?.original)
                    episodeSeason.text = t.seasonComplete()
                    episodeNumber.text = t.episodeComplete()
                    episodeDescription.text = t.description.parseAsHtml()

                }
            }
        })
    }
}