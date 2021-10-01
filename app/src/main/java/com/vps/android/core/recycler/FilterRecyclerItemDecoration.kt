package com.vps.android.core.recycler

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class FilterRecyclerItemDecoration(private val bottomSpaceHeight: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        if (parent.getChildAdapterPosition(view) == (parent.adapter?.itemCount ?: 0) - 1) {
            outRect.bottom = bottomSpaceHeight
        }
    }

}
