package com.api.tvmaze.presentation.fragments

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
import com.api.tvmaze.data.model.Show
import com.api.tvmaze.presentation.adapter.FavoriteShowAdapter
import com.api.tvmaze.presentation.viewModel.ShowViewModel

class FavoriteShowFragment : Fragment() {

    private lateinit var binding: FragmentFavoriteShowBinding
    private lateinit var model: ShowViewModel
    private lateinit var adapter: FavoriteShowAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        model = ViewModelProvider(requireActivity())[ShowViewModel::class.java]
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
        val favoritesShows = model.getFavoriteShow()
        adapter.updateList(favoritesShows)
    }

    private fun setupAdapter() {
        adapter = FavoriteShowAdapter(requireActivity(),
            callback = { show -> navigateToShowDetail(show) },
            menuCallback = { show, imgView -> popupMenu(show, imgView) })
        binding.rvFavorites.adapter = adapter
    }

    private fun popupMenu(show: Show, imgView: View) {

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

    private fun showDeleteConfirmationDialog(show: Show) {
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.apply {

            setTitle(R.string.remove_from_favorites)
            setPositiveButton(R.string.yes) { dialogInterface: DialogInterface, _: Int ->

                model.deleteFavoriteShow(show).observe(viewLifecycleOwner) { success ->
                    if (success) {
                        fetchFavorites()
                    }
                }

                dialogInterface.dismiss()
            }
            setNegativeButton(R.string.cancel) { dialogInterface: DialogInterface, _: Int ->
                dialogInterface.dismiss()
            }
        }

        val alertDialog: AlertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    private fun navigateToShowDetail(show: Show) {
        findNavController().navigate(R.id.action_favoriteShowFragment_to_showDetailFragment)
        model.response(show)
    }

}