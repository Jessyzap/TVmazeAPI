package com.api.tvmaze.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.api.tvmaze.R
import com.api.tvmaze.adapter.EpisodeListAdapter
import com.api.tvmaze.api.EpisodeAPI
import com.api.tvmaze.api.Network
import com.api.tvmaze.fragments.HomeFragment.Companion.URL
import com.api.tvmaze.model.Episode
import com.api.tvmaze.model.Season
import com.api.tvmaze.viewModel.ShowViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EpisodeFragment : Fragment() {

    private lateinit var model: ShowViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model = ViewModelProvider(requireActivity()).get(ShowViewModel::class.java)

    }

    fun getEpisodeAPI(id: Int) {

        val retrofitClient = Network.retrofitConfig(URL)
        val createRetrofit = retrofitClient.create(EpisodeAPI::class.java)

        val call = createRetrofit.getEpisodeAPI(id)

        call.enqueue(
            object : Callback<List<Episode>> {

                override fun onResponse(
                    call: Call<List<Episode>>,
                    response: Response<List<Episode>>
                ) {

                    val episodes = response.body()?.toList()

                    episodes?.let {

                        val rvEpisode = view?.findViewById<RecyclerView>(R.id.rvEpisode)
                        rvEpisode?.let { rvEpisode.layoutManager = LinearLayoutManager(context) }

                        val episodeListAdapter = EpisodeListAdapter(episodes, requireActivity())
                        rvEpisode?.adapter = episodeListAdapter

                        val loading = view?.findViewById<ProgressBar>(R.id.progressBarEpisodeList)
                        loading?.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<List<Episode>>, t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_episode, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        model.seasonLiveData.observe(viewLifecycleOwner, object : Observer<Season> {

            override fun onChanged(t: Season?) {
                t?.let {

                    getEpisodeAPI(t.id)
                }
            }
        })
    }
}