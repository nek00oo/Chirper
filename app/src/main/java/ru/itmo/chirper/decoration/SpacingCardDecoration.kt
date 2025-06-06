package ru.itmo.chirper.decoration

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpacingCardDecoration(private val verticalSpaceHeight: Int)
    : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State
    ) {
        outRect.bottom = verticalSpaceHeight

        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top = verticalSpaceHeight
        }
    }
}
