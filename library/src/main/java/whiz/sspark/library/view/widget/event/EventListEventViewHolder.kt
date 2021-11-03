package whiz.sspark.library.view.widget.event

import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.EventList
import whiz.sspark.library.data.static.DateTimePattern
import whiz.sspark.library.databinding.ViewEventListEventItemBinding
import whiz.sspark.library.extension.convertToDateString
import whiz.sspark.library.extension.show

class EventListEventViewHolder(
    private val binding: ViewEventListEventItemBinding
): RecyclerView.ViewHolder(binding.root) {
    fun init(event: EventList,
             onEventClicked: (String, String) -> Unit) {
        with(event) {
            binding.ivImage.show(imageUrl, R.drawable.ic_download_image_placeholder)
            binding.tvName.text = name
            binding.tvVenue.text = location
            binding.tvDate.text = startAt.convertToDateString(
                defaultPattern = DateTimePattern.dayAbbreviateMonthFormatEn,
                dayMonthThPattern = DateTimePattern.dayAbbreviateMonthFormatTh,
                yearThPattern = DateTimePattern.generalYear
            )

            binding.root.setOnClickListener {
                onEventClicked(id, imageUrl)
            }
        }
    }
}