package com.wynneplaga.beyondfilm.ui.favorites

import androidx.lifecycle.MediatorLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.kiwimob.firestore.livedata.livedata
import com.wynneplaga.beyondfilm.models.FilmData
import com.wynneplaga.beyondfilm.ui.FilmViewModel
import com.wynneplaga.beyondfilm.ui.home.HomeViewModel

class FavoritesViewModel(private val homeViewModel: HomeViewModel) : FilmViewModel() {

    private val favoriteFilms = FirebaseFirestore.getInstance()
            .collection("users")
            .document(FirebaseAuth.getInstance().uid!!)
            .collection("favorites")
            .livedata { it["id"] as String }
    private val filterBlock: (List<Any>) -> Unit = { _: List<Any> ->
        filmsToDisplay.value = favoriteFilms.value?.mapNotNull { faveFilm -> homeViewModel.filmsToDisplay.value?.find { it.id == faveFilm } }?.sortedBy(homeViewModel.sortBlock)
    }

    override val filmsToDisplay = MediatorLiveData<List<FilmData>>().apply {
        addSource(favoriteFilms, filterBlock)
        addSource(homeViewModel.filmsToDisplay, filterBlock)
    }

}