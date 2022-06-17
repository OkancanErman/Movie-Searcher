package com.oe.moviesearcher.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.RequestManager
import com.oe.moviesearcher.R
import com.oe.moviesearcher.adapters.MoviesAdapter
import com.oe.moviesearcher.databinding.MoviesFragmentBinding
import com.oe.moviesearcher.util.hide
import com.oe.moviesearcher.util.hideKeyboard
import com.oe.moviesearcher.util.show
import com.oe.moviesearcher.util.toast
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MoviesFragment : Fragment(), RecyclerViewClickListener {

    private var _binding: MoviesFragmentBinding? = null

    private val binding get() = _binding!!

    private val viewModel: MoviesViewModel by activityViewModels()

    @Inject
    lateinit var glide: RequestManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = MoviesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.details.observe(viewLifecycleOwner, Observer { movies->
            binding.recyclerviewMovies.also {
                it.layoutManager = LinearLayoutManager(requireContext())
                it.adapter = MoviesAdapter(movies, this, glide)
            }
        })

        lifecycleScope.launchWhenStarted {
            viewModel.eventFlow.collect { event ->
                when(event) {
                    is MoviesViewModel.Status.Loading -> {
                        binding.progressBar.show()
                        hideKeyboard()
                    }
                    is MoviesViewModel.Status.Success -> {
                        binding.progressBar.hide()
                    }
                    is MoviesViewModel.Status.Error -> {
                        event.message?.let { requireContext().toast(it) }
                        binding.progressBar.hide()
                    }
                }
            }
        }

        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onRecyclerViewItemClick(view: View, index: Int) {
        when(view.id){
            R.id.layout1 -> {
                val action = MoviesFragmentDirections.actionMoviesFragmentToDetailFragment(index)
                findNavController().navigate(action)
            }
        }
    }


}