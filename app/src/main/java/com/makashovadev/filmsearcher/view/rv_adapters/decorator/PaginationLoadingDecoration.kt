package com.makashovadev.filmsearcher.view.rv_adapters.decorator

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.core.view.children
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Pagination loading decoration for lists with uploaded items via pagination
 *
 * Usage:
 *
 * 1) Create an instance of decoration in your view model
 * 2) Set decoration in xml to recycler view using BindingAdapter("itemDecorator")
 * 3) When all items already loaded set showPaginationLoading = false from view model
 */
private const val BOTTOM_MARGIN = 120

class PaginationLoadingDecoration : RecyclerView.ItemDecoration() {

    var showPaginationLoading = true
    private var isProgressVisible = false

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        recyclerView: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, recyclerView, state)
        if (showPaginationLoading.not()) return

        when (isLastItem(recyclerView, view)) {
            true -> outRect.set(Rect(0, 0, 0, BOTTOM_MARGIN))
            else -> outRect.set(Rect(0, 0, 0, 0))
        }
    }

    private fun isLastItem(recyclerView: RecyclerView, view: View): Boolean {
        val lastItemPos = recyclerView.getChildAdapterPosition(view)
        return lastItemPos == recyclerView.adapter!!.itemCount - 1
    }

    override fun onDrawOver(canvas: Canvas, recyclerView: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(canvas, recyclerView, state)
        if (showPaginationLoading.not()) return

        val lastItem = recyclerView.children.lastOrNull()
        when (showLoading(recyclerView)) {
            true -> {
                PaginationProgressDrawer.drawSpinner(recyclerView, lastItem, canvas)
                isProgressVisible = true
            }

            else -> updateItems(recyclerView)
        }
    }

    private fun updateItems(recyclerView: RecyclerView) {

        recyclerView.children.lastOrNull()?.let {

            when (isLastItem(recyclerView, it)) {
                true -> it.measure(it.width, it.height)
                else -> if (isProgressVisible) {
                    isProgressVisible = false
                    recyclerView.invalidateItemDecorations()
                }
            }
        }
    }

    private fun showLoading(recyclerView: RecyclerView): Boolean {

        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val lastVisibleItemPos = layoutManager.findLastCompletelyVisibleItemPosition()

        return lastVisibleItemPos != -1 && lastVisibleItemPos >= recyclerView.adapter!!.itemCount - 1
    }
}