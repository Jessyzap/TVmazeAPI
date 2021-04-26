package com.api.tvmaze.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.parseAsHtml
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.api.tvmaze.R
import com.api.tvmaze.model.Episode
import com.api.tvmaze.model.Show
import com.api.tvmaze.viewModel.ShowViewModel
import kotlinx.android.synthetic.main.fragment_episode_detail.*
import kotlinx.android.synthetic.main.fragment_show_detail.*


class EpisodeDetailFragment : Fragment() {

    private lateinit var model: ShowViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model = ViewModelProvider(requireActivity()).get(ShowViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_episode_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        model.episodeLiveData.observe(viewLifecycleOwner, object : Observer<Episode> {

            override fun onChanged(t: Episode?) {
                t?.let {

                   // img_episode.load(t.image)
                    txt_episode_title.text = t.title
                    img_episode.load(t.image?.original)
                    txt_season_episode.text = t.seasonComplete()
                    txt_episode.text = t.episodeComplete()
                    txt_episode_description.text = t.description.parseAsHtml()

                }
            }
        })
    }
}