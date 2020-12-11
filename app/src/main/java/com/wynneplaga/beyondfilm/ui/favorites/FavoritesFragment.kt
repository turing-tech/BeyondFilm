package com.wynneplaga.beyondfilm.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import com.wynneplaga.beyondfilm.models.FilmData
import com.wynneplaga.beyondfilm.ui.FilmListFragment
import com.wynneplaga.beyondfilm.ui.FilmViewModel
import com.wynneplaga.beyondfilm.ui.home.HomeViewModel

class FavoritesFragment : FilmListFragment() {

    private lateinit var favoritesViewModel: FavoritesViewModel
    override val viewModel: FilmViewModel
        get() = favoritesViewModel
    override val directionRef: (FilmData) -> NavDirections
        get() = FavoritesFragmentDirections::actionNavigationFavoritesToDetailsFragment

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        favoritesViewModel =
                ViewModelProvider(this, object : ViewModelProvider.Factory {
                    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                        return FavoritesViewModel(ViewModelProvider(this@FavoritesFragment).get(HomeViewModel::class.java)) as T
                    }
                }).get(FavoritesViewModel::class.java)

        return super.onCreateView(inflater, container, savedInstanceState)
    }
}