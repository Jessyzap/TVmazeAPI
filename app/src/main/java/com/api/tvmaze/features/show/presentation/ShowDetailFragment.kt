package com.api.tvmaze.features.show.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.text.parseAsHtml
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.load
import com.api.tvmaze.R
import com.api.tvmaze.databinding.FragmentShowDetailBinding
import com.api.tvmaze.features.show.data.model.Show
import com.api.tvmaze.features.show.presentation.adapter.SeasonListAdapter
import com.api.tvmaze.features.show.presentation.viewModel.ShowViewModel
import com.api.tvmaze.utils.getParcelableArg
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShowDetailFragment : Fragment() {

    private lateinit var binding: FragmentShowDetailBinding
    private lateinit var model: ShowViewModel
    private lateinit var adapter: SeasonListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProvider(requireActivity())[ShowViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentShowDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAdapter()
        setArgs()
        observeViewModel()
        setupFavorite()
    }

    private fun setArgs() {
        getParcelableArg<Show>("Show")?.let { model.setShow(it) }
    }

    private fun setupFavorite() {
        val show = model.showLiveData.value
        model.checkIfIsFavorite(show?.id).observe(viewLifecycleOwner) { isFavorite ->
            show?.isFavorite = isFavorite
            updateFavoriteUI(isFavorite)
        }


        binding.imgFavorite.setOnClickListener {
            if (show?.isFavorite == true) {
                show.isFavorite = false
                updateFavoriteUI(false)
                show.let {
                    model.deleteFavoriteShow(show)
                }
            } else {
                show?.isFavorite = true
                updateFavoriteUI(true)
                show?.let {
                    model.saveFavoriteShow(show)
                }
            }
        }
    }

    private fun updateFavoriteUI(isFavorite: Boolean) {
        val tintListRes = if (isFavorite) R.color.pink else R.color.gray
        binding.imgFavorite.setColorFilter((ContextCompat.getColor(requireContext(), tintListRes)))
    }

    private fun setupAdapter() {
        adapter = SeasonListAdapter(requireContext()) { season ->
            model.setSeason(season)
            findNavController().navigate(R.id.action_showDetailFragment_to_episodeFragment)
        }
        binding.rvSeason.adapter = adapter
    }

    private fun observeViewModel() {
        model.showLiveData.observe(viewLifecycleOwner) { show ->
            show?.let {
                binding.apply {
                    txtGenre.text = show.genres.joinToString(separator = ", ")
                    imgShow.load(show.image?.medium)
                    txtSchedule.text = show.schedule.scheduleDetail()
                    txtTitle.text = show.name
                    txtDescription.text = show.summary?.parseAsHtml()
                }
                model.getSeasons(show.id)
            }
        }

        model.seasonLiveDataList.observe(viewLifecycleOwner) { seasonList ->
            seasonList?.let {

                binding.progressBarShowDetail.visibility = View.GONE

                val seasonBar = binding.seasonBar
                if (seasonList.size > 1) seasonBar.text = ("${seasonList.size} Seasons")

                adapter.updateList(seasonList)
            }
        }
    }
}