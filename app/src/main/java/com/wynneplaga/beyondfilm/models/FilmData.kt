package com.wynneplaga.beyondfilm.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilmData(val name: String, val imageSrc: String, val desc: String, var id: String) : Parcelable {

    constructor(): this("", "", "", "")

}