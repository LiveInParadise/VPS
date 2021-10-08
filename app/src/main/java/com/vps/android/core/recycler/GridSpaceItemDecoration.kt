package com.vps.android.core.recycler

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridSpaceItemDecoration(private val spaceHeight: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        val childPosition = parent.getChildAdapterPosition(view)
        outRect.top = spaceHeight
        outRect.left = spaceHeight

        if (childPosition % 2 != 0) {
            outRect.right = spaceHeight * 2
        }
    }

}
