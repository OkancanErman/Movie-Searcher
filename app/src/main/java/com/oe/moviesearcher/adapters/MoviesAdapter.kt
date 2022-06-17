package com.oe.moviesearcher.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.oe.moviesearcher.R
import com.oe.moviesearcher.databinding.RecyclerviewMovieBinding
import com.oe.moviesearcher.fragment.RecyclerViewClickListener
import com.oe.moviesearcher.network.MovieResponse

class MoviesAdapter(private val movies: List<MovieResponse>, private val listener: RecyclerViewClickListener, private val glide: RequestManager) : RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder>() {

    override fun getItemCount() = movies.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        MoviesViewHolder(DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.recyclerview_movie,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        holder.recyclerviewMovieBinding.movie = movies[position]
        holder.recyclerviewMovieBinding.root.setOnClickListener {
            listener.onRecyclerViewItemClick(holder.recyclerviewMovieBinding.root, position)
        }
        if (!movies[position].poster.isNullOrEmpty())
            glide.load(movies[position].poster).into(holder.recyclerviewMovieBinding.imageView)
    }

    inner class MoviesViewHolder(
        val recyclerviewMovieBinding: RecyclerviewMovieBinding
    ) : RecyclerView.ViewHolder(recyclerviewMovieBinding.root)
}