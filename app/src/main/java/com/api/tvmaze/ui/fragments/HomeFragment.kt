package com.api.tvmaze.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.api.tvmaze.databinding.FragmentHomeBinding
import com.api.tvmaze.model.Search
import com.api.tvmaze.model.Show
import com.api.tvmaze.ui.MainActivity
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        launch()

        searchButton.setOnClickListener {
            val path = query.text.toString()
            model.getSearch(path)
            view?.hideKeyboard()
        }

        model.searchLiveDataList.observe(viewLifecycleOwner,
            Observer<List<Search>> { searchList ->
                loading.visibility = View.GONE

                val SearchListAdapter =
                    searchList?.let { SearchListAdapter(searchList, requireActivity()) }
                rvHome.adapter = SearchListAdapter
                SearchListAdapter?.notifyDataSetChanged()
            })

        model.showLiveDataList.observe(viewLifecycleOwner,
            Observer<List<Show>> { showList ->
                loading.visibility = View.GONE

                val homeListAdapter = showList?.let { HomeListAdapter(showList, requireActivity()) }
                rvHome.adapter = homeListAdapter
                homeListAdapter?.notifyDataSetChanged()
            })
    }

    private fun View.hideKeyboard() {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun launch() {
        model.getShows()
    }

    override fun onResume() {
        super.onResume()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            activity?.finish()
        }
    }
}

