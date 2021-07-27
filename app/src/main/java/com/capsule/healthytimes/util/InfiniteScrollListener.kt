package com.capsule.healthytimes.util

import androidx.recyclerview.widget.RecyclerView

class InfiniteListScrollListener(
    private val direction: Direction = Direction.DOWN,
    val loadNextPage: () -> Unit
) : RecyclerView.OnScrollListener() {

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if ((direction == Direction.DOWN && !recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) ||
            (direction == Direction.UP && !recyclerView.canScrollVertically(-1) && newState == RecyclerView.SCROLL_STATE_IDLE)
        ) {
            loadNextPage()
        }
    }

    enum class Direction { UP, DOWN }
}
