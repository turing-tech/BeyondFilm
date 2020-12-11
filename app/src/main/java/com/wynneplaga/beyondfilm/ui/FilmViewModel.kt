package com.wynneplaga.beyondfilm.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.wynneplaga.beyondfilm.models.FilmData

abstract class FilmViewModel: ViewModel() {

    abstract val filmsToDisplay: LiveData<List<FilmData>>

}