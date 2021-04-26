package com.api.tvmaze.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.parseAsHtml
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.api.tvmaze.R
import com.api.tvmaze.ui.adapter.SeasonListAdapter
import com.api.tvmaze.api.Network
import com.api.tvmaze.api.SeasonAPI
import com.api.tvmaze.ui.fragments.HomeFragment.Companion.URL
import com.api.tvmaze.model.Season
import com.api.tvmaze.model.Show
import com.api.tvmaze.viewModel.ShowViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ShowDetailFragment : Fragment() {

    private lateinit var model: ShowViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model = ViewModelProvider(requireActivity()).get(ShowViewModel::class.java)

    }

    fun getSeasonAPI(id: Int) {

        val retrofitClient = Network.retrofitConfig(URL)
        val createRetrofit = retrofitClient.create(SeasonAPI::class.java)

        val call = createRetrofit.getSeasonAPI(id)

        call.enqueue(
            object : Callback<List<Season>> {

                override fun onResponse(
                    call: Call<List<Season>>,
                    response: Response<List<Season>>
                ) {

                    val seasons = response.body()?.toList()

                    seasons?.let {

                        val rvSeason = view?.findViewById<RecyclerView>(R.id.rvSeason)
                        rvSeason?.let { rvSeason.layoutManager = LinearLayoutManager(context) }

                        val seasonListAdapter = SeasonListAdapter(seasons, requireActivity())
                        rvSeason?.adapter = seasonListAdapter

                        val loading = view?.findViewById<ProgressBar>(R.id.progressBarShowDetail)
                        loading?.visibility = View.GONE


                        if (seasons.size > 1) {

                            val seasonBar = view?.findViewById<TextView>(R.id.season_bar)

                            val size = "${seasons.size} Seasons"
                            seasonBar?.text = size
                        }
                    }
                }

                override fun onFailure(call: Call<List<Season>>, t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_show_detail, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val showTitle = view?.findViewById<TextView>(R.id.txt_title)
        val showImage = view?.findViewById<ImageView>(R.id.img_show)
        val showGenre = view?.findViewById<TextView>(R.id.txt_genre)
        val showSchedule = view?.findViewById<TextView>(R.id.txt_schedule)
        val showDescription = view?.findViewById<TextView>(R.id.txt_description)


        model.showLiveData.observe(viewLifecycleOwner, object : Observer<Show> {

            override fun onChanged(t: Show?) {
                t?.let {

                    showGenre?.text = t.genre.joinToString(separator = ", ")
                    showImage?.load(t.image?.medium)
                    showSchedule?.text = t.schedule.scheduleDetail()
                    showTitle?.text = t.title
                    showDescription?.text = t.description.parseAsHtml()

                    getSeasonAPI(t.id)
                }
            }
        })
    }
}