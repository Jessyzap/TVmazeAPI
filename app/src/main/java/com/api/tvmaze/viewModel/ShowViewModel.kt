package com.api.tvmaze.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.api.tvmaze.api.*
import com.api.tvmaze.model.Episode
import com.api.tvmaze.model.Search
import com.api.tvmaze.model.Season
import com.api.tvmaze.model.Show
import retrofit2.Call

class ShowViewModel : ViewModel() {

    val showLiveData = MutableLiveData<Show>()
    val seasonLiveData = MutableLiveData<Season>()
    val episodeLiveData = MutableLiveData<Episode>()

    fun response(show: Show) {
        showLiveData.value = show
    }

    fun responseSeason(season: Season) {
        seasonLiveData.value = season
    }

    fun responseEpisode(episode: Episode) {
        episodeLiveData.value = episode
    }

    companion object {
        const val URL = "https://api.tvmaze.com"
    }

    val retrofitClient = Network.retrofitConfig(URL)


    val call: Call<List<Show>> = retrofitClient.create(ShowAPI::class.java).getShowAPI()

    val callSeason = retrofitClient.create(SeasonAPI::class.java)

    val callEpisode = retrofitClient.create(EpisodeAPI::class.java)

    val callSearch = retrofitClient.create(SearchAPI::class.java)
}