package com.api.tvmaze.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.parseAsHtml
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.api.tvmaze.R
import com.api.tvmaze.model.Episode
import com.api.tvmaze.viewModel.ShowViewModel


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


        val episodeTitle = view?.findViewById<TextView>(R.id.txt_episode_title)
        val episodeImage = view?.findViewById<ImageView>(R.id.img_episode)
        val episodeSeason = view?.findViewById<TextView>(R.id.txt_season_episode)
        val episodeNumber = view?.findViewById<TextView>(R.id.txt_episode)
        val episodeDescription = view?.findViewById<TextView>(R.id.txt_episode_description)


        model.episodeLiveData.observe(viewLifecycleOwner, object : Observer<Episode> {

            override fun onChanged(t: Episode?) {
                t?.let {

                    episodeTitle?.text = t.title
                    episodeImage?.load(t.image?.original)
                    episodeSeason?.text = t.seasonComplete()
                    episodeNumber?.text = t.episodeComplete()
                    episodeDescription?.text = t.description.parseAsHtml()

                }
            }
        })
    }
}