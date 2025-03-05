package com.api.tvmaze.features.show.presentation

import android.os.Bundle
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
import com.api.tvmaze.features.show.data.model.Show
import com.api.tvmaze.features.show.presentation.adapter.HomeListAdapter
import com.api.tvmaze.features.show.presentation.viewModel.ShowViewModel
import com.api.tvmaze.utils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ShowHomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var model: ShowViewModel
    private var adapter: HomeListAdapter? = null
    private var loadStateListener: ((CombinedLoadStates) -> Unit)? = null

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

        setupAdapter()
        fetchShows()
        setupRefresh()
        setupSearch()
        handleState()
        searchObserver()
    }

    private fun setupRefresh() {
        binding.swp.setOnRefreshListener {
            fetchShows(true)
            binding.searchView.setQuery("", false)
        }
    }

    private fun setupSearch() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                model.onSearchQueryChanged(newText.orEmpty())
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
        lifecycleScope.launch {
            model.pagingData.collectLatest { pagingData ->
                adapter?.submitData(pagingData)
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
        if (forceFetch || model.searchLiveDataList.value.isNullOrEmpty()) {
            model.clearSearch()
            model.getShows()
            binding.swp.isRefreshing = false
            submitShowListToPagingAdapter()
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