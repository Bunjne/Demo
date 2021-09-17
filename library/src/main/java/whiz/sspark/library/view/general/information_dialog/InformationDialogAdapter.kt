package whiz.sspark.library.view.general.information_dialog

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.CalendarInformationIndex
import whiz.sspark.library.data.entity.JuniorOutcome
import whiz.sspark.library.data.entity.SeniorOutcome
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
        val item = items.getOrNull(position) ?: Item()

        item.juniorOutcome?.let {
            (holder.itemView as JuniorExpectedOutcomeItemView).apply {
                init(
                    number = it.number,
                    description = it.description
                )

                if (position != items.lastIndex) {
                    setPadding(0, 0, 0, 9.toDP(context))
                }
            }
        }

        item.seniorOutcome?.let {
            (holder.itemView as SeniorExpectedOutcomeItemView).apply {
                init(
                    level = it.level,
                    description = it.description
                )

                if (position != items.lastIndex) {
                    setPadding(0, 0, 0, 9.toDP(context))
                }
            }
        }

        item.calendarInformationIndex?.let {
            (holder.itemView as CalendarColorInformationItemView).apply {
                init(
                    color = it.color,
                    description = it.description
                )

                if (position != items.lastIndex) {
                    setPadding(0, 0, 0, 9.toDP(context))
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            items[position].juniorOutcome != null -> InformationDialogItemType.JUNIOR_OUTCOME_ITEM_TYPE.type
            items[position].seniorOutcome != null -> InformationDialogItemType.SENIOR_OUTCOME_ITEM_TYPE.type
            items[position].calendarInformationIndex != null -> InformationDialogItemType.CALENDAR_COLOR_INFO_ITEM_TYPE.type
            else -> InformationDialogItemType.CALENDAR_COLOR_INFO_ITEM_TYPE.type
        }
    }

    override fun getItemCount() = items.size

    data class Item(
        val juniorOutcome: JuniorOutcome? = null,
        val seniorOutcome: SeniorOutcome? = null,
        val calendarInformationIndex: CalendarInformationIndex? = null
    )
}