package com.api.tvmaze.features.favorite_show.presentation

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.api.tvmaze.R
import com.api.tvmaze.databinding.FragmentFavoriteShowBinding
import com.api.tvmaze.features.favorite_show.presentation.adapter.FavoriteShowAdapter
import com.api.tvmaze.features.favorite_show.presentation.viewModel.FavoriteShowViewModel
import com.api.tvmaze.features.show.domain.entity.ShowEntity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteShowFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteShowBinding
    private lateinit var model: FavoriteShowViewModel
    private lateinit var adapter: FavoriteShowAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProvider(this)[FavoriteShowViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteShowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapter()
        fetchFavorites()
    }

    private fun fetchFavorites() {
        model.getFavoriteShow()
        model.favoriteShows.observe(viewLifecycleOwner) { favoritesShows ->
            adapter.updateList(favoritesShows)
        }
    }

    private fun setupAdapter() {
        adapter = FavoriteShowAdapter(requireActivity(),
            callback = { show -> navigateToShowDetail(show) },
            menuCallback = { show, imgView -> popupMenu(show, imgView) })
        binding.rvFavorites.adapter = adapter
    }

    private fun popupMenu(show: ShowEntity, imgView: View) {

        val popupMenu = PopupMenu(requireContext(), imgView)
        popupMenu.menuInflater.inflate(R.menu.favorite_menu, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                R.id.favoriteDelete -> {
                    showDeleteConfirmationDialog(show)
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }

    private fun showDeleteConfirmationDialog(show: ShowEntity) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.apply {

            setTitle(R.string.remove_from_favorites)
            setPositiveButton(R.string.yes) { dialogInterface: DialogInterface, _: Int ->
                model.deleteFavoriteShow(show)
                dialogInterface.dismiss()
            }
            setNegativeButton(R.string.cancel) { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            }
        }

        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun navigateToShowDetail(show: ShowEntity) {
        val bundle = Bundle().apply { putParcelable("Show", show) }
        findNavController().navigate(R.id.action_favoriteShowFragment_to_showDetailFragment, bundle)
    }

}