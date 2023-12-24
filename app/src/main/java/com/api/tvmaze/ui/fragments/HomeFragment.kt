package com.api.tvmaze.ui.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.api.tvmaze.R
import com.api.tvmaze.databinding.FragmentHomeBinding
import com.api.tvmaze.model.Show
import com.api.tvmaze.ui.adapter.HomeListAdapter
import com.api.tvmaze.utils.hideKeyboard
import com.api.tvmaze.viewModel.ShowViewModel


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var model: ShowViewModel
    private lateinit var adapter: HomeListAdapter
    private var forceArg: Boolean = false
    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProvider(requireActivity())[ShowViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getArgs()
        setupAdapter()
        launch(force = forceArg)
        setupSearch()
        observer()
    }

    private fun getArgs() {
        forceArg = arguments?.getBoolean("force") ?: false
    }

    private fun setupSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                runnable?.let { handler.removeCallbacks(it) }

                if (newText != model.currentSearchQuery && (newText?.length ?: 0) >= 2) {
                    runnable = Runnable {
                        model.getSearch(newText.orEmpty())
                    }

                    runnable?.let { handler.postDelayed(it, 800) }
                    model.currentSearchQuery = newText
                }
                return true
            }
        })
    }

    private fun setupAdapter() {
        adapter = HomeListAdapter(requireActivity()) { show -> navigateToShowDetail(show) }
        binding.rvHome.adapter = adapter
    }

    private fun setPlaceholder(list: List<Show>) {
        if (list.isEmpty()) {
            binding.placeholder.visibility = View.VISIBLE
            adapter.updateList(emptyList())
        } else {
            binding.placeholder.visibility = View.GONE
            view?.hideKeyboard()
        }

    }

    private fun observer() {
        model.showLiveDataList.observe(viewLifecycleOwner) { showList ->
            binding.progressBarHome.visibility = View.GONE

            showList?.let {
                adapter.updateList(it)
                setPlaceholder(showList)
            }
        }

        model.searchLiveDataList.observe(viewLifecycleOwner) { searchList ->
            binding.progressBarHome.visibility = View.GONE

            searchList?.let {
                adapter.updateList(it)
                setPlaceholder(searchList)
            }
        }
    }

    private fun navigateToShowDetail(show: Show) {
        findNavController().navigate(R.id.action_homeFragment_to_showDetailFragment)
        model.response(show)
    }

    private fun launch(force: Boolean = false) {
        if (model.showLiveDataList.value.isNullOrEmpty() && model.searchLiveDataList.value.isNullOrEmpty() || force) {
            if (force) {
                model.showLiveDataList.value = null
                model.searchLiveDataList.value = null
                adapter.updateList(emptyList())
            }
            model.getShows()
        }
    }

    override fun onPause() {
        super.onPause()
        arguments?.clear()
    }

}