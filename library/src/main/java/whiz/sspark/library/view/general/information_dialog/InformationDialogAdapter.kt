package whiz.sspark.library.view.general.information_dialog

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.CalendarInformationIndex
import whiz.sspark.library.data.entity.JuniorOutcomeIndex
import whiz.sspark.library.data.entity.SeniorOutcomeIndex
import whiz.sspark.library.extension.toDP
import whiz.sspark.library.view.general.information_dialog.item.CalendarColorInformationItemView
import whiz.sspark.library.view.general.information_dialog.item.JuniorExpectedOutcomeItemView
import whiz.sspark.library.view.general.information_dialog.item.SeniorExpectedOutcomeItemView

class InformationDialogAdapter(private val context: Context,
                               private val items: List<Item>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    class JuniorOutComeViewHolder(view: View): RecyclerView.ViewHolder(view)
    class SeniorOutComeViewHolder(view: View): RecyclerView.ViewHolder(view)
    class CalendarColorInformationViewHolder(view: View): RecyclerView.ViewHolder(view)

    enum class InformationDialogItemType(val type: Int) {
        JUNIOR_OUTCOME_ITEM_TYPE(0),
        SENIOR_OUTCOME_ITEM_TYPE(1),
        CALENDAR_COLOR_INFO_ITEM_TYPE(2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            InformationDialogItemType.JUNIOR_OUTCOME_ITEM_TYPE.type ->
                JuniorOutComeViewHolder(
                    JuniorExpectedOutcomeItemView(context).apply {
                        layoutParams = RecyclerView.LayoutParams(
                            RecyclerView.LayoutParams.MATCH_PARENT,
                            RecyclerView.LayoutParams.WRAP_CONTENT
                        )
                    }
                )

            InformationDialogItemType.SENIOR_OUTCOME_ITEM_TYPE.type ->
                SeniorOutComeViewHolder(
                    SeniorExpectedOutcomeItemView(context).apply {
                        layoutParams = RecyclerView.LayoutParams(
                            RecyclerView.LayoutParams.MATCH_PARENT,
                            RecyclerView.LayoutParams.WRAP_CONTENT
                        )
                    }
                )

            else ->
                CalendarColorInformationViewHolder(
                    CalendarColorInformationItemView(context).apply {
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

        item?.juniorOutcomeIndex?.let {
            (holder.itemView as JuniorExpectedOutcomeItemView).init(
                number = it.number,
                description = it.description
            )
        }

        item?.seniorOutcomeIndex?.let {
            (holder.itemView as SeniorExpectedOutcomeItemView).init(
                level = it.level,
                description = it.description
            )
        }

        item?.calendarInformationIndex?.let {
            (holder.itemView as CalendarColorInformationItemView).init(
                color = it.color,
                description = it.description
            )
        }

        if (position != items.lastIndex) {
            holder.itemView.setPadding(0, 0, 0, 9.toDP(context))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            items[position].juniorOutcomeIndex != null -> InformationDialogItemType.JUNIOR_OUTCOME_ITEM_TYPE.type
            items[position].seniorOutcomeIndex != null -> InformationDialogItemType.SENIOR_OUTCOME_ITEM_TYPE.type
            items[position].calendarInformationIndex != null -> InformationDialogItemType.CALENDAR_COLOR_INFO_ITEM_TYPE.type
            else -> InformationDialogItemType.CALENDAR_COLOR_INFO_ITEM_TYPE.type
        }
    }

    override fun getItemCount() = items.size

    data class Item(
        val juniorOutcomeIndex: JuniorOutcomeIndex? = null,
        val seniorOutcomeIndex: SeniorOutcomeIndex? = null,
        val calendarInformationIndex: CalendarInformationIndex? = null
    )
}