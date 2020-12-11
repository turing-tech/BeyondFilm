package com.wynneplaga.beyondfilm.ui

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.diff.FastAdapterDiffUtil
import com.turingtechnologies.materialscrollbar.AlphabetIndicator
import com.wynneplaga.beyondfilm.NamedFastAdapter
import com.wynneplaga.beyondfilm.databinding.FragmentHomeBinding
import com.wynneplaga.beyondfilm.models.FilmData
import java.util.*
import kotlin.concurrent.timerTask
import kotlin.math.roundToInt

abstract class FilmListFragment: Fragment() {

    protected abstract val viewModel: FilmViewModel
    protected abstract val directionRef: (FilmData) -> NavDirections
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val filmsAdapter = ItemAdapter<FilmCardItem>()
        viewModel.filmsToDisplay.value?.apply {
            filmsAdapter.add(map(::FilmCardItem))
        }
        val adapter = NamedFastAdapter<FilmCardItem>{ item -> item.filmData.name }.apply { addAdapter(0, filmsAdapter) }
        viewModel.filmsToDisplay.observe(viewLifecycleOwner) { list ->
            FastAdapterDiffUtil[filmsAdapter] = list.map(::FilmCardItem)
        }
        adapter.onClickListener = { v, _, item, _ ->
            findNavController().navigate(directionRef(item.filmData))
            true
        }
        binding.filmsRecyclerView.adapter = adapter
        (binding.filmsRecyclerView.layoutManager as GridLayoutManager).spanCount = (resources.displayMetrics.xdpi / 300).roundToInt()
        binding.filmsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            val timer = Timer()
            var task: TimerTask? = null

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                when (newState) {
                    RecyclerView.SCROLL_STATE_DRAGGING -> {
                        task?.cancel()
                        timer.purge()
                        task = null

                        if (binding.scrollBar.alpha != 1f) {
                            ObjectAnimator.ofFloat(binding.scrollBar.alpha, 1f).apply {
                                addUpdateListener { binding.scrollBar.alpha = it.animatedValue as Float }
                                duration = 250L * (1L - binding.scrollBar.alpha.toLong())
                                start()
                            }
                        }
                    }
                    RecyclerView.SCROLL_STATE_IDLE -> {
                        task = timerTask {
                            if (_binding?.scrollBar?.isDragging ?: true) return@timerTask
                            activity?.runOnUiThread {
                                ObjectAnimator.ofFloat(1f, 0f).apply {
                                    addUpdateListener { _binding?.scrollBar?.alpha = it.animatedValue as Float }
                                    duration = 250
                                    start()
                                }
                            }
                        }
                        timer.schedule(task, 750)
                    }
                }
            }
        })
        binding.scrollBar.setIndicator(AlphabetIndicator(context), true)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}