package com.api.tvmaze.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.api.tvmaze.R
import com.api.tvmaze.adapter.HomeListAdapter
import com.api.tvmaze.api.ShowAPI
import com.api.tvmaze.api.Network
import com.api.tvmaze.model.Show
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getShowAPI()

    }

    fun getShowAPI() {

        val retrofitClient = Network.retrofitConfig("https://api.tvmaze.com")
        val createRetrofit = retrofitClient.create(ShowAPI::class.java)
        val call = createRetrofit.getShowAPI()

        call.enqueue(
            object : Callback<List<Show>> {

                override fun onResponse(call: Call<List<Show>>, response: Response<List<Show>>) {

                    val shows = response.body()?.toList()

                    shows?.let {
                        val homeListAdapter = HomeListAdapter(shows, requireActivity())
                        rvHome.adapter = homeListAdapter
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


        val rvHome = view?.findViewById<RecyclerView>(R.id.rvHome)
        rvHome?.let { rvHome.layoutManager = GridLayoutManager(context, 2) }
    }
}