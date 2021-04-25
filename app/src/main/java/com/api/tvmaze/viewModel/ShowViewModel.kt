package com.api.tvmaze.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.api.tvmaze.model.Episode
import com.api.tvmaze.model.Season
import com.api.tvmaze.model.Show

class ShowViewModel : ViewModel() {

    val showLiveData = MutableLiveData<Show>()
    val seasonLiveData = MutableLiveData<Season>()
    val episodeLiveData = MutableLiveData<Episode>()

    fun response (show: Show) {
        showLiveData.value = show
    }

    fun responseSeason (season: Season) {
        seasonLiveData.value = season
    }

    fun responseEpisode (episode: Episode) {
        episodeLiveData.value = episode
    }
}