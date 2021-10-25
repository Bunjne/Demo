package whiz.sspark.library.view.widget.menu

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.MenuSegment

class MenuSegmentAdapter(private val context: Context,
                         private val segments: List<MenuSegment>,
                         private val onSegmentSelected: (String) -> Unit): RecyclerView.Adapter<MenuSegmentAdapter.ViewHolder>() {
    private var selectedTab = 0

    companion object {
        const val VIEW_TYPE = 1
    }

    fun resetSelectedTab() {
        selectedTab = 0
        notifyItemChanged(0)
    }

    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(MenuSegmentView(context).apply {
            layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        (holder.itemView as? MenuSegmentView)?.init(
            selectTab = selectedTab,
            segments = segments,
            onSegmentSelected = {
                if (selectedTab != it) {
                    selectedTab = it
                    onSegmentSelected(segments[it].type.type)
                }
            }
        )
    }

    override fun getItemCount() = 1
}