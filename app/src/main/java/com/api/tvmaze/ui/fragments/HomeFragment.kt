package com.api.tvmaze.ui.fragments

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.api.tvmaze.databinding.FragmentHomeBinding
import com.api.tvmaze.model.Search
import com.api.tvmaze.model.Show
import com.api.tvmaze.ui.adapter.HomeListAdapter
import com.api.tvmaze.ui.adapter.SearchListAdapter
import com.api.tvmaze.viewModel.ShowViewModel


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
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

    private val loading by lazy {
        binding.progressBarHome
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        launch()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        searchButton.setOnClickListener {


            val callSearch = query.text.toString()
            val launchSearch = SearchTask(callSearch)
            launchSearch.execute()

            // dismiss keyboard
            view?.hideKeyboard()
        }

        rvHome.layoutManager = GridLayoutManager(activity, 2)
    }


    inner class ShowsTask() : AsyncTask<Void, Void, List<Show>?>() {

        override fun onPreExecute() {
            super.onPreExecute()

        }

        override fun doInBackground(vararg params: Void?): List<Show>? {

            try {
                // call response
                return model.call.execute().body()

            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                return null
            }
        }

        override fun onPostExecute(result: List<Show>?) {
            super.onPostExecute(result)

            try {
                // progress bar invisible
                loading.visibility = View.GONE

                // put result in adapter
                val homeListAdapter = result?.let { HomeListAdapter(it, requireActivity()) }
                rvHome.adapter = homeListAdapter
                homeListAdapter?.notifyDataSetChanged()

            } catch (e: Exception) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    inner class SearchTask(callSearch: String) : AsyncTask<Void, Void, List<Search>?>() {

        private val search = callSearch

        override fun doInBackground(vararg params: Void): List<Search>? {

            return model.callSearch.getShowSearchAPI(search).execute().body()
        }

        override fun onPostExecute(result: List<Search>?) {
            super.onPostExecute(result)

            loading.visibility = View.GONE

            val searchListAdapter = result?.let { SearchListAdapter(it, requireActivity()) }
            rvHome.adapter = searchListAdapter
            searchListAdapter?.notifyDataSetChanged()
        }
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun launch() {
        val shows = ShowsTask()
        shows.execute()
    }
}


