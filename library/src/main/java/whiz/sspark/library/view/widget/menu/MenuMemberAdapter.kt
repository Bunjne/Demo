package whiz.sspark.library.view.widget.menu

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.MenuMember
import whiz.sspark.library.utility.getItemPositionType

class MenuMemberAdapter(private val context: Context,
                        private val members: List<MenuMember>,
                        private val onMemberClicked: (MenuMember) -> Unit): RecyclerView.Adapter<MenuMemberAdapter.ViewHolder>() {

    companion object {
        const val VIEW_TYPE = 2222
    }

    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(MenuMemberView(context).apply {
            layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val member = members.getOrNull(position)
        member?.let {
            (holder.itemView as? MenuMemberView)?.apply {
                init(it, onMemberClicked)
                background = when {
                    members.size == 1 -> ContextCompat.getDrawable(context, R.drawable.bg_base_single_shadow_large)
                    position == 0 -> ContextCompat.getDrawable(context, R.drawable.bg_base_top_shadow_large)
                    position == members.lastIndex -> ContextCompat.getDrawable(context, R.drawable.bg_base_bottom_shadow_large)
                    else ->  ContextCompat.getDrawable(context, R.drawable.bg_base_middle_shadow_large)
                }
            }
        }
    }


    override fun getItemCount() = members.size
}