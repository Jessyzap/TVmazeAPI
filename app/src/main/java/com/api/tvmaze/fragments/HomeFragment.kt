package com.api.tvmaze.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.api.tvmaze.R
import com.api.tvmaze.adapter.HomeListAdapter
import com.api.tvmaze.api.Network
import com.api.tvmaze.api.SearchAPI
import com.api.tvmaze.api.ShowAPI
import com.api.tvmaze.model.Show
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {

    companion object {
        const val URL = "https://api.tvmaze.com"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getShowAPI("000")

    }

    fun getShowAPI(callHome: String) {

//        val retrofitClient = Network.retrofitConfig("https://api.tvmaze.com")
//        val createRetrofit = retrofitClient.create(ShowAPI::class.java)
//        val call = createRetrofit.getShowAPI()

        val call: Call<List<Show>>

        call = if (callHome == "000") {
            val retrofitClient = Network.retrofitConfig(URL)
            val createRetrofit = retrofitClient.create(ShowAPI::class.java)
            createRetrofit.getShowAPI()

        } else {

            val retrofitClient = Network.retrofitConfig(URL)
            val createRetrofit = retrofitClient.create(SearchAPI::class.java)
            createRetrofit.getShowSearchAPI(callHome)
        }


        call.enqueue(
            object : Callback<List<Show>> {

                override fun onResponse(call: Call<List<Show>>, response: Response<List<Show>>) {

                    val shows = response.body()?.toList()

                    shows?.let {

                        val rvHome = view?.findViewById<RecyclerView>(R.id.rvHome)
                        rvHome?.let { rvHome.layoutManager = GridLayoutManager(context, 2) }

                        val homeListAdapter = HomeListAdapter(shows, requireActivity())
                        rvHome?.adapter = homeListAdapter

                        val loading = view?.findViewById<ProgressBar>(R.id.progressBarHome)
                        loading?.visibility = View.GONE
                    }
                }

                override fun onFailure(call: Call<List<Show>>, t: Throwable) {
                    Toast.makeText(context, t.message, Toast.LENGTH_SHORT).show()
                }
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val imageSearch = view?.findViewById<ImageView>(R.id.img_search)
        val editSearch = view?.findViewById<EditText>(R.id.edt_search)

        imageSearch?.setOnClickListener {

            val callSearch = editSearch?.text.toString()

            getShowAPI(callSearch)

        }
    }
}

