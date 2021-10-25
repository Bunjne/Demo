package whiz.sspark.library.view.widget.collaboration.class_group

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.R
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
        CLASS_GROUP(2)
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
                item.navigationBarItems != null -> {
                    (holder.itemView as? ClassGroupNavigationBarView)?.init(item.navigationBarItems, onNavigationBarItemClicked)
                }
                else -> {
                    (holder.itemView as? ClassGroupHeaderBarView)?.apply {
                        init(
                            title = item.headerBarTitle ?: "",
                            iconUrl = item.headerBarIcon ?: "",
                            gradientColor = item.gradientColor ?: intArrayOf(ContextCompat.getColor(context, R.color.primaryStartColor), ContextCompat.getColor(context, R.color.primaryEndColor))
                        )

                        setPadding(6.toDP(context), 10.toDP(context), 6.toDP(context), 10.toDP(context))
                    }
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = items[position]
        return when {
            item.navigationBarItems != null -> ClassGroupItemType.NAVIGATION_BAR.type
            item.classGroupCourse != null -> ClassGroupItemType.CLASS_GROUP.type
            else -> ClassGroupItemType.HEADER_BAR.type
        }
    }

    override fun getItemCount(): Int = items.size

    data class Item(
        val navigationBarItems: List<BottomNavigationBarItem>? = null,
        val headerBarTitle: String? = null,
        val headerBarIcon: String? = null,
        val gradientColor: IntArray? = null,
        val classGroupCourse: ClassGroupCourse? = null,
    )
}