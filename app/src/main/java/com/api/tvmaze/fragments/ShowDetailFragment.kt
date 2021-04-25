package com.api.tvmaze.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.parseAsHtml
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.api.tvmaze.viewModel.ShowViewModel
import com.api.tvmaze.R
import com.api.tvmaze.adapter.HomeListAdapter
import com.api.tvmaze.adapter.SeasonListAdapter
import com.api.tvmaze.api.Network
import com.api.tvmaze.api.SeasonAPI
import com.api.tvmaze.api.ShowAPI
import com.api.tvmaze.model.Season
import com.api.tvmaze.model.Show
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_show_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class ShowDetailFragment : Fragment() {

    private lateinit var model: ShowViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        model = ViewModelProvider(requireActivity()).get(ShowViewModel::class.java)

    }

    fun getSeasonAPI(id: Int) {

        val retrofitClient = Network.retrofitConfig("https://api.tvmaze.com")
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
                        val seasonListAdapter =
                            SeasonListAdapter(seasons, requireActivity())
                        rvSeason.adapter = seasonListAdapter
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

        model.showLiveData.observe(viewLifecycleOwner, object : Observer<Show> {

            override fun onChanged(t: Show?) {
                t?.let {

                    txt_genre.text = t.genre.joinToString(separator = ", ")
                    //img_show.load(t.image)
                    txt_title.text = t.title
                    txt_description.text = t.description.parseAsHtml()
                    // txt_release.text = t.time

                    getSeasonAPI(t.id)
                }
            }
        })

        val rvSeason = view?.findViewById<RecyclerView>(R.id.rvSeason)
        rvSeason?.let { rvSeason.layoutManager = LinearLayoutManager(context) }
    }
}