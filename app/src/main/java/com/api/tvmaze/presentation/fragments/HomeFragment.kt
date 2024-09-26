package com.api.tvmaze.presentation.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.api.tvmaze.R
import com.api.tvmaze.databinding.FragmentHomeBinding
import com.api.tvmaze.data.model.Show
import com.api.tvmaze.presentation.adapter.HomeListAdapter
import com.api.tvmaze.utils.hideKeyboard
import com.api.tvmaze.presentation.viewModel.ShowViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var model: ShowViewModel
    private var adapter: HomeListAdapter? = null
    private var forceFetchArg: Boolean = false
    private val handler = Handler(Looper.getMainLooper())
    private var runnable: Runnable? = null
    private var shouldFetchShow: Boolean = false
    private var loadStateListener: ((CombinedLoadStates) -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProvider(requireActivity())[ShowViewModel::class.java]
        shouldFetchShow = true
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
        fetchShows(forceFetch = forceFetchArg)
        setupSearch()
        submitShowListToPagingAdapter()
        handleState()
        searchObserver()
    }

    private fun getArgs() {
        forceFetchArg = arguments?.getBoolean("force") ?: false
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
        } else {
            binding.placeholder.visibility = View.GONE
            view?.hideKeyboard()
        }
    }

    private fun submitShowListToPagingAdapter() {
        if (model.searchLiveDataList.value.isNullOrEmpty()) {
            lifecycleScope.launch {
                model.pagingData.collectLatest { pagingData ->
                    adapter?.submitData(pagingData)
                }
            }
        }
    }

    private fun searchObserver() {
        model.searchLiveDataList.observe(viewLifecycleOwner) { searchList ->
            lifecycleScope.launch {
                searchList?.let {
                    adapter?.submitData(PagingData.from(searchList))
                    setPlaceholder(searchList)
                }
            }
        }
    }

    private fun handleState() {
        loadStateListener = { loadState: CombinedLoadStates ->
            when (loadState.refresh) {
                is LoadState.Loading -> {
                    binding.progressBarHome.visibility = View.VISIBLE
                }

                is LoadState.Error -> {
                    binding.progressBarHome.visibility = View.GONE
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.error_message),
                        LENGTH_SHORT
                    ).show()
                }

                is LoadState.NotLoading -> {
                    binding.progressBarHome.visibility = View.GONE
                }
            }
        }
        loadStateListener?.let { adapter?.addLoadStateListener(it) }
    }

    private fun navigateToShowDetail(show: Show) {
        findNavController().navigate(R.id.action_homeFragment_to_showDetailFragment)
        model.setShow(show)
    }

    private fun fetchShows(forceFetch: Boolean = false) {
        if (forceFetch || shouldFetchShow) {
            model.clearSearch()
            model.getShows()
            shouldFetchShow = false
        }
    }

    override fun onPause() {
        super.onPause()
        arguments?.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        loadStateListener?.let { adapter?.removeLoadStateListener(it) }
    }

}