package com.api.tvmaze.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.core.text.parseAsHtml
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.load
import com.api.tvmaze.R
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
    ): View {

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


        model.showLiveData.observe(viewLifecycleOwner,
            Observer<Show> { show ->
                show?.let {
                    showGenre.text = show.genres.joinToString(separator = ", ")
                    showImage.load(show.image?.medium)
                    showSchedule.text = show.schedule.scheduleDetail()
                    showTitle.text = show.name
                    showDescription.text = show.summary.parseAsHtml()

                    model.getSeasons(show.id)
                    loading.visibility = View.GONE
                }
            })

        model.seasonLiveDataList.observe(viewLifecycleOwner,
            Observer<List<Season>> { seasonList ->
                val seasonBar = binding.seasonBar

                seasonList?.let {
                    if (seasonList.size > 1) seasonBar.text = ("${seasonList.size} Seasons")

                    val seasonListAdapter = SeasonListAdapter(seasonList, requireActivity())
                    rvSeason.adapter = seasonListAdapter
                    seasonListAdapter.notifyDataSetChanged()
                }
            })
    }

    override fun onResume() {
        super.onResume()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().navigate(R.id.homeFragment)
        }
    }
}


