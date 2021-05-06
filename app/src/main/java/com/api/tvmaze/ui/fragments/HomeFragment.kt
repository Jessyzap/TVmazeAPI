package com.api.tvmaze.ui.fragments

import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.api.tvmaze.databinding.FragmentHomeBinding
import com.api.tvmaze.model.Show
import com.api.tvmaze.ui.adapter.HomeListAdapter
import com.api.tvmaze.viewModel.ShowViewModel


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding get() = _binding!!
    private val model = ShowViewModel()


    private val rvHome by lazy {
        binding.rvHome
    }

    private val searchButton by lazy {
        binding.imgSearch
    }

    private val query by lazy {
        binding.edtSearch
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        launch()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
        //return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        searchButton.setOnClickListener {

            val callSearch = query.text.toString()

            val a = model.callSearch.getShowSearchAPI(callSearch).execute().body()

            //val homeListAdapter = HomeListAdapter(a, requireActivity())
           // rvHome.adapter = homeListAdapter
        }

        rvHome.layoutManager = GridLayoutManager(activity, 2)
    }


    inner class ShowsTask() : AsyncTask<Void, Void, List<Show>?>() {


        override fun onPreExecute() {
            super.onPreExecute()
        }

        override fun doInBackground(vararg params: Void?): List<Show>? {

            // call response
            return model.call.execute().body()

        }

        override fun onPostExecute(result: List<Show>?) {
            super.onPostExecute(result)

            val loading = binding.progressBarHome

                // progress bar invisible
                loading.visibility = View.GONE

                // put result in adapter
                val homeListAdapter = result?.let { HomeListAdapter(it, requireActivity()) }
                rvHome.adapter = homeListAdapter
        }
    }

    fun launch() {
        val shows = ShowsTask()
        shows.execute()
    }
}


