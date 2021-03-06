package whiz.sspark.library.view.widget.menu

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.MenuMemberItem
import whiz.sspark.library.extension.setDarkModeBackground
import whiz.sspark.library.view.widget.base.ItemListTitleView
import whiz.sspark.library.view.widget.base.MemberWithRightArrowView
import java.lang.Exception

class AdviseeGuardianAdapter(private val onMemberClicked: (MenuMemberItem) -> Unit): ListAdapter<AdviseeGuardianAdapter.AdviseeGuardianItem, RecyclerView.ViewHolder>(MenuMemberDiffCallback()) {

    companion object {
        const val TITLE_VIEW_TYPE = 1111
        const val GUARDIAN_VIEW_TYPE = 2222
    }

    override fun getItemViewType(position: Int): Int {
        val item = try {
            getItem(position)
        } catch (e: Exception) {
            return TITLE_VIEW_TYPE
        }

        return when (item) {
            is AdviseeGuardianItem.Guardian -> GUARDIAN_VIEW_TYPE
            is AdviseeGuardianItem.Title -> TITLE_VIEW_TYPE
        }
    }

    class GuardianViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    class TitleViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == GUARDIAN_VIEW_TYPE) {
            GuardianViewHolder(MemberWithRightArrowView(parent.context).apply {
                layoutParams = RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            })
        } else {
            TitleViewHolder(ItemListTitleView(parent.context).apply {
                layoutParams = RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            })
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        val isNextItemHeader = getItemViewType(position + 1) == TITLE_VIEW_TYPE
        val isPreviousItemHeader = getItemViewType(position - 1) == TITLE_VIEW_TYPE

        when (item) {
            is AdviseeGuardianItem.Guardian -> {
                (holder.itemView as? MemberWithRightArrowView)?.apply {
                    init(
                        member = item.member.convertToMemberItem(),
                        onMemberClicked = {
                            onMemberClicked(item.member)
                        }
                    )

                    setDarkModeBackground(
                        isNextItemHeader = isNextItemHeader,
                        isPreviousItemHeader = isPreviousItemHeader
                    )
                }
            }
            is AdviseeGuardianItem.Title -> (holder.itemView as ItemListTitleView).init(item.title)
        }
    }

    sealed class AdviseeGuardianItem {
        data class Guardian(val member: MenuMemberItem): AdviseeGuardianItem()
        data class Title(val title: String): AdviseeGuardianItem()
    }

    private class MenuMemberDiffCallback : DiffUtil.ItemCallback<AdviseeGuardianItem>() {
        override fun areItemsTheSame(oldItem: AdviseeGuardianItem, newItem: AdviseeGuardianItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: AdviseeGuardianItem, newItem: AdviseeGuardianItem): Boolean {
            return oldItem == newItem
        }
    }
}