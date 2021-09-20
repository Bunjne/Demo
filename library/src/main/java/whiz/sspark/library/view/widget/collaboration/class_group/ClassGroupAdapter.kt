package whiz.sspark.library.view.widget.collaboration.class_group

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.BottomNavigationBarItem
import whiz.sspark.library.data.entity.ClassGroupCourse
import whiz.sspark.library.extension.toDP
import whiz.sspark.library.view.widget.collaboration.class_group.class_group_navigation_bar.ClassGroupNavigationBarView

class ClassGroupAdapter(private val context: Context,
                        private val items: List<Item>,
                        private val onNavigationBarItemClicked: (Int) -> Unit,
                        private val onClassGroupItemClicked: (ClassGroupCourse) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    enum class ClassGroupItemType(val type: Int) {
        NAVIGATION_BAR(0),
        HEADER_BAR(1),
        ITEM(2)
    }

    class ClassGroupNavigationBarViewHolder(view: View): RecyclerView.ViewHolder(view)

    class ClassGroupHeaderBarViewHolder(view: View): RecyclerView.ViewHolder(view)

    class ClassGroupItemViewHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ClassGroupItemType.NAVIGATION_BAR.type -> ClassGroupNavigationBarViewHolder(
                ClassGroupNavigationBarView(context).apply {
                    layoutParams = RecyclerView.LayoutParams(
                        RecyclerView.LayoutParams.MATCH_PARENT,
                        RecyclerView.LayoutParams.WRAP_CONTENT
                    )
            })
            ClassGroupItemType.HEADER_BAR.type -> ClassGroupHeaderBarViewHolder(
                ClassGroupHeaderBarView(context).apply {
                    layoutParams = RecyclerView.LayoutParams(
                        RecyclerView.LayoutParams.MATCH_PARENT,
                        RecyclerView.LayoutParams.WRAP_CONTENT
                    )
                }
            )
            else -> ClassGroupItemViewHolder(
                ClassGroupItemView(context).apply {
                    layoutParams = RecyclerView.LayoutParams(
                        RecyclerView.LayoutParams.MATCH_PARENT,
                        RecyclerView.LayoutParams.WRAP_CONTENT
                    )
                }
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items.getOrNull(position)

        item?.let {
            when {
                item.classGroupCourse != null -> {
                    (holder.itemView as? ClassGroupItemView)?.init(item.classGroupCourse, onClassGroupItemClicked)
                }
                item.navigationBarItem != null -> {
                    (holder.itemView as? ClassGroupNavigationBarView)?.apply {
                        init(item.navigationBarItem, onNavigationBarItemClicked)
                    }
                }
                else -> {
                    (holder.itemView as? ClassGroupHeaderBarView)?.apply {
                        init(
                            title = item.headerBarTitle ?: "",
                            iconUrl = item.headerBarIcon ?: "",
                            startColor = item.headerBarStartColor ?: Color.BLACK,
                            endColor = item.headerBarEndColor ?: Color.BLACK
                        )

                        setPadding(6.toDP(context), 16.toDP(context), 6.toDP(context), 16.toDP(context))
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = items[position]
        return when {
            item.navigationBarItem != null -> ClassGroupItemType.NAVIGATION_BAR.type
            item.classGroupCourse != null -> ClassGroupItemType.ITEM.type
            else -> ClassGroupItemType.HEADER_BAR.type
        }
    }

    override fun getItemCount(): Int = items.size

    data class Item(
        val navigationBarItem: List<BottomNavigationBarItem>? = null,
        val headerBarTitle: String? = null,
        val headerBarIcon: String? = null,
        val headerBarStartColor: Int? = null,
        val headerBarEndColor: Int? = null,
        val classGroupCourse: ClassGroupCourse? = null,
    )
}