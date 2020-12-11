package com.wynneplaga.beyondfilm.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.mikepenz.fastadapter.binding.AbstractBindingItem
import com.wynneplaga.beyondfilm.R
import com.wynneplaga.beyondfilm.databinding.ItemFilmCardBinding
import com.wynneplaga.beyondfilm.models.FilmData

class FilmCardItem(val filmData: FilmData): AbstractBindingItem<ItemFilmCardBinding>() {

    override val type: Int
        get() = R.id.film_card_item

    override fun bindView(binding: ItemFilmCardBinding, payloads: List<Any>) {
        binding.title.text = filmData.name
        val shimmer = Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
                .setDuration(900) // how long the shimmering animation takes to do one full sweep
                .setBaseAlpha(0.7f) //the alpha of the underlying children
                .setHighlightAlpha(0.9f) // the shimmer alpha amount
                .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                .setAutoStart(true)
                .build()
        Glide
            .with(binding.filmImage)
            .load(filmData.imageSrc)
            .placeholder(ShimmerDrawable().apply { setShimmer(shimmer) })
            .into(binding.filmImage)
    }

    override fun createBinding(inflater: LayoutInflater, parent: ViewGroup?): ItemFilmCardBinding {
        return ItemFilmCardBinding.inflate(inflater, parent, false)
    }

    override fun equals(other: Any?): Boolean {
        return (other as? FilmCardItem ?: return false).filmData == filmData
    }

}