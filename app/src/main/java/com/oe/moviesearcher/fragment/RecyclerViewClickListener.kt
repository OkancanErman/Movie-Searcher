package com.oe.moviesearcher.fragment

import android.view.View

interface RecyclerViewClickListener {
    fun onRecyclerViewItemClick(view: View, index: Int)
}