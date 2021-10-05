package whiz.sspark.library.view.widget.menu

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.CalendarWidgetItem
import whiz.sspark.library.data.entity.GradeSummary
import whiz.sspark.library.data.entity.MenuItem
import whiz.sspark.library.data.entity.PreviewMessageItem
import whiz.sspark.library.data.enum.MenuItemType
import whiz.sspark.library.extension.setDarkModeBackground
import whiz.sspark.library.view.widget.base.ItemListTitleView
import java.lang.IndexOutOfBoundsException

class MenuAdapter(private val context: Context,
                  private val onMenuClicked: (String) -> Unit): ListAdapter<MenuAdapter.Item, RecyclerView.ViewHolder>(MenuDiffCallback()) {

    private var requiredHeight = 0

    private fun notifyWidgetHeightChange() {
        listOf(
            MenuItemType.ADVISING_WIDGET.type,
            MenuItemType.NOTIFICATION_WIDGET.type,
            MenuItemType.CALENDAR_WIDGET.type,
            MenuItemType.GRADE_SUMMARY.type
        ).forEach { type ->
            val index = currentList.indexOfFirst { it.type == type }

            if (index != -1) {
                notifyItemChanged(index)
            }
        }
    }

    fun resetHeight() {
        requiredHeight = 0
    }

    companion object {
        const val TITLE_TYPE = 3
        const val MENU_TYPE = 4
        const val MESSAGE_WIDGET_TYPE = 5
        const val NO_MESSAGE_WIDGET_TYPE = 6
        const val CALENDAR_WIDGET_TYPE = 7
        const val GRADE_SUMMARY_WIDGET_TYPE = 8
        const val DOWNLOAD_FAILED_WIDGET_TYPE = 9
        const val CONTACT_VIEW_TYPE = 10
    }

    override fun getItemViewType(position: Int): Int {
        val item = try {
            getItem(position)
        } catch (e: IndexOutOfBoundsException) {
            return TITLE_TYPE
        }

        return when {
            item.isShowDownloadFailedWidget -> DOWNLOAD_FAILED_WIDGET_TYPE
            item.type == MenuItemType.MENU.type -> MENU_TYPE
            item.type == MenuItemType.CONTACT.type -> CONTACT_VIEW_TYPE
            item.type == MenuItemType.ADVISING_WIDGET.type ||
            item.type == MenuItemType.NOTIFICATION_WIDGET.type -> if (!item.previewMessageItem?.title.isNullOrBlank()) {
                MESSAGE_WIDGET_TYPE
            } else {
                NO_MESSAGE_WIDGET_TYPE
            }
            item.type == MenuItemType.CALENDAR_WIDGET.type -> CALENDAR_WIDGET_TYPE
            item.type == MenuItemType.GRADE_SUMMARY.type -> GRADE_SUMMARY_WIDGET_TYPE
            else -> TITLE_TYPE
        }
    }

    class TitleViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class ContactViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class MenuViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class DownloadFailedViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class NoMessageWidgetViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class MessageWidgetViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class CalendarWidgetViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class GradeSummaryWidgetViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MENU_TYPE -> MenuViewHolder(MenuItemView(context).apply {
                layoutParams = RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            })
            MESSAGE_WIDGET_TYPE -> MessageWidgetViewHolder(MenuPreviewMessageWidget(context).apply {
                layoutParams = RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            })
            NO_MESSAGE_WIDGET_TYPE -> NoMessageWidgetViewHolder(MenuPreviewNoMessageWidget(context).apply {
                layoutParams = RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            })
            CALENDAR_WIDGET_TYPE -> CalendarWidgetViewHolder(MenuCalendarWidget(context).apply {
                layoutParams = RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            })
            GRADE_SUMMARY_WIDGET_TYPE -> GradeSummaryWidgetViewHolder(MenuGradeSummaryWidget(context).apply {
                layoutParams = RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            })
            DOWNLOAD_FAILED_WIDGET_TYPE -> DownloadFailedViewHolder(MenuDownloadFailedWidget(context).apply {
                layoutParams = RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            })
            CONTACT_VIEW_TYPE -> ContactViewHolder(MenuContactItemView(context).apply {
                layoutParams = RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            })
            else -> TitleViewHolder(ItemListTitleView(context).apply {
                layoutParams = RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            })
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        val viewType = getItemViewType(position)
        val isNextItemHeader = getItemViewType(position + 1) != MENU_TYPE
        val isPreviousItemHeader = getItemViewType(position - 1) != MENU_TYPE

        when(viewType) {
            MENU_TYPE -> {
                (holder.itemView as? MenuItemView)?.apply {
                    init(item.menuItem!!)
                    setOnClickListener {
                        onMenuClicked(item.code)
                    }
                    setDarkModeBackground(isNextItemHeader, isPreviousItemHeader)
                }
            }
            MESSAGE_WIDGET_TYPE -> {
                (holder.itemView as? MenuPreviewMessageWidget)?.apply {
                    init(item.title, item.previewMessageItem!!)
                    setOnClickListener {
                        onMenuClicked(item.code)
                    }
                }
                holder.itemView.post {
                    val height = holder.itemView.height
                    if (height > requiredHeight) {
                        requiredHeight = height
                        notifyWidgetHeightChange()
                    } else {
                        holder.itemView.layoutParams.height = requiredHeight
                    }
                }
            }
            NO_MESSAGE_WIDGET_TYPE -> {
                (holder.itemView as? MenuPreviewNoMessageWidget)?.apply {
                    init(item.title)
                    setOnClickListener {
                        onMenuClicked(item.code)
                    }
                }
                holder.itemView.post {
                    val height = holder.itemView.height
                    if (height > requiredHeight) {
                        requiredHeight = height
                        notifyWidgetHeightChange()
                    } else {
                        holder.itemView.layoutParams.height = requiredHeight
                    }
                }
            }
            CALENDAR_WIDGET_TYPE -> {
                (holder.itemView as? MenuCalendarWidget)?.apply {
                    init(item.calendarItem!!)
                    setOnClickListener {
                        onMenuClicked(item.code)
                    }
                }
                holder.itemView.post {
                    val height = holder.itemView.height
                    if (height > requiredHeight) {
                        requiredHeight = height
                        notifyWidgetHeightChange()
                    } else {
                        holder.itemView.layoutParams.height = requiredHeight
                    }
                }
            }
            GRADE_SUMMARY_WIDGET_TYPE -> {
                (holder.itemView as? MenuGradeSummaryWidget)?.apply {
                    init(item.title, item.gradeSummary!!)
                    setOnClickListener {
                        onMenuClicked(item.code)
                    }
                }
                holder.itemView.post {
                    val height = holder.itemView.height
                    if (height > requiredHeight) {
                        requiredHeight = height
                        notifyWidgetHeightChange()
                    } else {
                        holder.itemView.layoutParams.height = requiredHeight
                    }
                }
            }
            DOWNLOAD_FAILED_WIDGET_TYPE -> {
                holder.itemView.post {
                    val height = holder.itemView.height
                    if (height > requiredHeight) {
                        requiredHeight = height
                        notifyWidgetHeightChange()
                    } else {
                        holder.itemView.layoutParams.height = requiredHeight
                    }
                }
            }
            CONTACT_VIEW_TYPE -> {
                (holder.itemView as? MenuContactItemView)?.init(item.title) {
                    onMenuClicked(item.code)
                }
            }
            TITLE_TYPE -> {
                (holder.itemView as? ItemListTitleView)?.init(item.title)
            }
        }
    }

    data class Item(
        val type: String,
        val code: String,
        val title: String,
        var isShowDownloadFailedWidget: Boolean = false,
        val menuItem: MenuItem? = null,
        var calendarItem: CalendarWidgetItem? = null,
        var previewMessageItem: PreviewMessageItem? = null,
        var gradeSummary: List<GradeSummary>? = null
    )
}

private class MenuDiffCallback : DiffUtil.ItemCallback<MenuAdapter.Item>() {
    override fun areItemsTheSame(oldItem: MenuAdapter.Item, newItem: MenuAdapter.Item): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: MenuAdapter.Item, newItem: MenuAdapter.Item): Boolean {
        val sameGradeSummary = if (oldItem.gradeSummary != null && newItem.gradeSummary != null) {
            oldItem.gradeSummary!!.containsAll(newItem.gradeSummary!!) && newItem.gradeSummary!!.containsAll(oldItem.gradeSummary!!)
        } else {
            !(oldItem.gradeSummary != null || newItem.gradeSummary != null)
        }

        return oldItem.type == newItem.type &&
                oldItem.code == newItem.code &&
                oldItem.title == newItem.title &&
                oldItem.menuItem == newItem.menuItem &&
                oldItem.calendarItem == newItem.calendarItem &&
                oldItem.previewMessageItem == newItem.previewMessageItem &&
                sameGradeSummary
    }
}