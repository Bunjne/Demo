package whiz.sspark.library.view.widget.menu

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.MenuMember
import whiz.sspark.library.view.widget.base.MemberWithRightArrowView

class MenuMemberAdapter(private val context: Context,
                        private val onMemberClicked: (MenuMember) -> Unit): ListAdapter<MenuMember, MenuMemberAdapter.ViewHolder>(MenuMemberDiffCallback()) {

    companion object {
        const val VIEW_TYPE = 2222
    }

    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(MemberWithRightArrowView(context).apply {
            layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val member = getItem(position)
        member?.let {
            (holder.itemView as? MemberWithRightArrowView)?.apply {
                init(
                    member = it.convertToMemberItem(),
                    onMemberClicked = {
                        onMemberClicked(it)
                    }
                )

                background = when {
                    itemCount == 1 -> ContextCompat.getDrawable(context, R.drawable.bg_base_item_list_single)
                    position == 0 -> ContextCompat.getDrawable(context, R.drawable.bg_base_item_list_top)
                    position == itemCount - 1 -> ContextCompat.getDrawable(context, R.drawable.bg_base_item_list_bottom)
                    else ->  ContextCompat.getDrawable(context, R.drawable.bg_base_item_list_middle)
                }
            }
        }
    }
}

private class MenuMemberDiffCallback : DiffUtil.ItemCallback<MenuMember>() {
    override fun areItemsTheSame(oldItem: MenuMember, newItem: MenuMember): Boolean {
        return oldItem == newItem
    }
    override fun areContentsTheSame(oldItem: MenuMember, newItem: MenuMember): Boolean {
        return oldItem == newItem
    }
}