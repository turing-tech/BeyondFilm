package com.wynneplaga.beyondfilm.ui.details

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.transition.TransitionInflater
import com.airbnb.lottie.LottieCompositionFactory
import com.airbnb.lottie.LottieDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.wynneplaga.beyondfilm.MainActivity
import com.wynneplaga.beyondfilm.R
import com.wynneplaga.beyondfilm.databinding.FragmentFilmDetailsBinding

class FilmDetailsFragment : Fragment() {

    private lateinit var viewModel: FilmDetailsViewModel
    private var _binding: FragmentFilmDetailsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(R.transition.image)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val filmData = FilmDetailsFragmentArgs.fromBundle(requireArguments()).filmData
        viewModel = ViewModelProvider(this).get(filmData.id, FilmDetailsViewModel::class.java)

        _binding = FragmentFilmDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        postponeEnterTransition()

        val likeDrawable = LottieDrawable().apply {
            scale = .06f
            setMinFrame(30)
        }
        LottieCompositionFactory.fromRawRes(requireContext(), R.raw.like).addListener(likeDrawable::setComposition)
        binding.favoriteButton.scaleType = ImageView.ScaleType.CENTER
        binding.favoriteButton.setImageDrawable(likeDrawable)
        FirebaseFirestore
                .getInstance()
                .collection("users")
                .document(FirebaseAuth.getInstance().currentUser!!.uid)
                .collection("favorites")
                .get()
                .addOnSuccessListener {
                    if (it.find { it["id"] == filmData.id }?.apply { viewModel.favoritedId = id } != null) {
                        likeDrawable.frame = likeDrawable.maxFrame.toInt()
                    }
                }

        filmData.apply {
            (activity as? MainActivity)?.supportActionBar?.title = name
            binding.titleText.text = name
            binding.descText.text = desc
            Glide
                .with(requireContext())
                .load(imageSrc)
                .listener(object : RequestListener<Drawable?> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable?>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        startPostponedEnterTransition()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable?>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        startPostponedEnterTransition()
                        return false
                    }
                })
                .into(binding.titleImage)
            binding.favoriteButton.setOnClickListener {
                likeDrawable.frame = likeDrawable.minFrame.toInt()
                if (viewModel.favoritedId.isEmpty()) {
                    likeDrawable.playAnimation()
                    FirebaseFirestore
                            .getInstance()
                            .collection("users")
                            .document(FirebaseAuth.getInstance().currentUser!!.uid)
                            .collection("favorites")
                            .add(object { var id = this@apply.id })
                            .addOnSuccessListener {
                                likeDrawable.playAnimation()
                                viewModel.favoritedId = it.id
                            }
                } else {
                    likeDrawable.endAnimation()
                    FirebaseFirestore
                            .getInstance()
                            .collection("users")
                            .document(FirebaseAuth.getInstance().currentUser!!.uid)
                            .collection("favorites")
                            .document(viewModel.favoritedId)
                            .delete()
                            .addOnSuccessListener {
                                viewModel.favoritedId = ""
                            }
                }
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}