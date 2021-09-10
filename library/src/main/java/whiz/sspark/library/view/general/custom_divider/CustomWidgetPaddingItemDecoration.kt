package whiz.sspark.library.view.general.custom_divider

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager

class CustomWidgetPaddingItemDecoration(private val padding : Int,
                                        private val viewTypes: List<Int>) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val viewType = parent.getChildViewHolder(view).itemViewType

        val gridLayoutManager = parent.layoutManager as GridLayoutManager?
        val spanCount = gridLayoutManager!!.spanCount
        val params = view.layoutParams as GridLayoutManager.LayoutParams
        val spanIndex = params.spanIndex
        val spanSize = params.spanSize

        if (viewTypes.contains(viewType)) {
            if (spanIndex == 0) {
                outRect.left = padding
            } else if (spanIndex + spanSize == spanCount) {
                outRect.right = padding
            }
        }
    }
}