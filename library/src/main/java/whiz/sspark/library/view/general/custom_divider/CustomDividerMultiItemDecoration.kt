package whiz.sspark.library.view.general.custom_divider

import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import androidx.core.view.forEachIndexed
import java.lang.Exception

class CustomDividerMultiItemDecoration(private val divider: Drawable,
                                       private val dividerViewType: List<Int>) : RecyclerView.ItemDecoration() {
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        c.save()
        val dividerLeft = parent.paddingLeft
        val dividerRight = parent.width - parent.paddingRight

        parent.forEachIndexed { index, view ->

            val viewType = parent.getChildViewHolder(view).itemViewType
            val nextPosition = index + 1
            if (nextPosition < parent.childCount) {
                val nextView = parent.getChildAt(nextPosition)
                val nextType = parent.getChildViewHolder(nextView).itemViewType

                if (dividerViewType.contains(viewType) && dividerViewType.contains(nextType)) {
                    val params = view.layoutParams as RecyclerView.LayoutParams
                    val dividerTop = view.bottom + params.bottomMargin
                    val dividerBottom = dividerTop + divider.intrinsicHeight

                    divider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom)
                    divider.draw(c)
                }
            }
        }

        c.restore()
    }

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val viewType = parent.getChildViewHolder(view).itemViewType

        val nextViewType = try {
            val nextPosition = parent.getChildAdapterPosition(view) + 1
            parent.adapter?.getItemViewType(nextPosition)
        } catch (e: Exception) {
            -1
        }

        if (dividerViewType.contains(viewType) && dividerViewType.contains(nextViewType)) {
            outRect.bottom = divider.intrinsicHeight
        }
    }
}