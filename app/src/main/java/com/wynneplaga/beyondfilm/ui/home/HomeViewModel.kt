package com.wynneplaga.beyondfilm.ui.home

import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import com.google.firebase.firestore.FirebaseFirestore
import com.kiwimob.firestore.livedata.livedata
import com.wynneplaga.beyondfilm.models.FilmData
import com.wynneplaga.beyondfilm.ui.FilmViewModel

class HomeViewModel : FilmViewModel() {

    val sortBlock: (FilmData) -> String = { it.name.removePrefix("The").trim() }

    override val filmsToDisplay = FirebaseFirestore
        .getInstance()
        .collection("films")
        .livedata()
        .map {
            it
                .documents
                .map { doc -> doc.toObject(FilmData::class.java)!!.apply { id = doc.id } }
                .sortedBy(sortBlock)
        }
        .distinctUntilChanged()

}