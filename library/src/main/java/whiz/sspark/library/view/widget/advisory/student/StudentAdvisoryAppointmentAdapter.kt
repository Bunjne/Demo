package whiz.sspark.library.view.widget.advisory.student

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.AdvisorySlot
import whiz.sspark.library.extension.toDP
import whiz.sspark.library.view.widget.base.ItemListTitleView

class StudentAdvisoryAppointmentAdapter(private val onSelectStatusClicked: (View, AdvisorySlot) -> Unit) : ListAdapter<StudentAdvisoryAppointmentAdapter.AppointmentItem, RecyclerView.ViewHolder>(DiffCallback()){

    companion object {
        const val APPOINTMENT_VIEW_TYPE = 1111
        const val TITLE_VIEW_TYPE = 2222
    }

    override fun getItemViewType(position: Int): Int {
        val item = try {
            getItem(position)
        } catch (e: IndexOutOfBoundsException) {
            return TITLE_VIEW_TYPE
        }

        return if (item is AppointmentItem.AppointmentSlot) {
            APPOINTMENT_VIEW_TYPE
        } else {
            TITLE_VIEW_TYPE
        }
    }

    private class AppointmentSlotViewHolder(view: View): RecyclerView.ViewHolder(view)

    private class TitleViewHolder(view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            APPOINTMENT_VIEW_TYPE -> AppointmentSlotViewHolder(StudentAdvisoryAppointmentSlotItemView(parent.context).apply {
                    layoutParams = RecyclerView.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                })
            else -> TitleViewHolder(ItemListTitleView(parent.context).apply {
                layoutParams = RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            })
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)

        when (item) {
            is AppointmentItem.AppointmentSlot -> (holder.itemView as? StudentAdvisoryAppointmentSlotItemView)?.apply {
                init(item.advisoryAppointment, onSelectStatusClicked)

                setPadding(6.toDP(context), 0, 6.toDP(context), 0)
            }
            is AppointmentItem.Title -> (holder.itemView as? ItemListTitleView)?.apply {
                init(item.title)

                setPadding(0, 6.toDP(context), 0, 4.toDP(context))
            }
        }
    }

    sealed class AppointmentItem {
        data class AppointmentSlot(val advisoryAppointment: AdvisorySlot): AppointmentItem()
        data class Title(val title: String): AppointmentItem()
    }

    private class DiffCallback : DiffUtil.ItemCallback<AppointmentItem>() {
        override fun areItemsTheSame(oldItem: AppointmentItem, newItem: AppointmentItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: AppointmentItem, newItem: AppointmentItem): Boolean {
            return oldItem == newItem
        }
    }
}