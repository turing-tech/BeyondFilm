package com.wynneplaga.beyondfilm.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavDirections
import com.wynneplaga.beyondfilm.models.FilmData
import com.wynneplaga.beyondfilm.ui.FilmListFragment
import com.wynneplaga.beyondfilm.ui.FilmViewModel

class HomeFragment : FilmListFragment() {

    private lateinit var homeViewModel: HomeViewModel
    override val viewModel: FilmViewModel
        get() = homeViewModel
    override val directionRef: (FilmData) -> NavDirections
        get() = HomeFragmentDirections::actionNavigationHomeToDetailsFragment

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

}