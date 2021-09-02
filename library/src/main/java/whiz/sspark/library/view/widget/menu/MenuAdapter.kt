package whiz.sspark.library.view.widget.menu

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.CalendarWidgetItem
import whiz.sspark.library.data.entity.MenuItem
import whiz.sspark.library.data.entity.PreviewMessageItem
import whiz.sspark.library.data.enum.MenuItemType

class MenuAdapter(private val context: Context,
                  private val onMenuClicked: () -> Unit): ListAdapter<MenuAdapter.Item, RecyclerView.ViewHolder>(MenuDiffCallback()) {

    companion object {
        const val TITLE_TYPE = 3333
        const val MENU_TYPE = 4444
        const val MESSAGE_WIDGET_TYPE = 5555
        const val NO_MESSAGE_WIDGET_TYPE = 6666
        const val CALENDAR_WIDGET_TYPE = 7777
    }

    override fun getItemViewType(position: Int): Int { //TODO wait confirm
        val item = getItem(position)
        return when(item.type) {
            MenuItemType.DEFAULT -> MENU_TYPE
            MenuItemType.ADVISING_WIDGET -> if (item.previewMessageItem != null) {
                MESSAGE_WIDGET_TYPE
            } else {
                NO_MESSAGE_WIDGET_TYPE
            }
            MenuItemType.NOTIFICATION_WIDGET -> MESSAGE_WIDGET_TYPE
            MenuItemType.CALENDAR_WIDGET -> CALENDAR_WIDGET_TYPE
            else -> TITLE_TYPE
        }
    }

    class TitleViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class MenuViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class MessageWidgetViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class CalendarWidgetViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class NoMessageWidgetViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MenuViewHolder(MenuMemberView(context).apply {
            layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        })
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

    }

    data class Item(
        val type: MenuItemType,
        val title: String? = null,
        val menuItem: MenuItem? = null,
        val calendarItem: CalendarWidgetItem? = null,
        val previewMessageItem: PreviewMessageItem? = null
    )
}

private class MenuDiffCallback : DiffUtil.ItemCallback<MenuAdapter.Item>() {
    override fun areItemsTheSame(oldItem: MenuAdapter.Item, newItem: MenuAdapter.Item): Boolean {
        return oldItem == newItem
    }
    override fun areContentsTheSame(oldItem: MenuAdapter.Item, newItem: MenuAdapter.Item): Boolean {
        return oldItem == newItem
    }
}