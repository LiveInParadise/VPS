package com.vps.android.core.recycler

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MainHorizontalSpaceItemDecoration(private val horizontalSpaceHeight: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.left = horizontalSpaceHeight
        outRect.top = horizontalSpaceHeight
        outRect.bottom = horizontalSpaceHeight
        if (parent.getChildAdapterPosition(view) == (parent.adapter?.itemCount ?: 0) - 1) {
            outRect.right = horizontalSpaceHeight
        }
    }

}
