package whiz.sspark.library.view.widget.class_schedule.all_class

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import whiz.sspark.library.data.entity.ClassScheduleAllClass
import whiz.sspark.library.databinding.ViewClassScheduleAllClassItemBinding

class ClassScheduleAllClassAdapter: ListAdapter<ClassScheduleAllClass, ClassScheduleAllClassViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassScheduleAllClassViewHolder {
        return ClassScheduleAllClassViewHolder(ViewClassScheduleAllClassItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ClassScheduleAllClassViewHolder, position: Int) {
        val course = getItem(position)
        holder.init(
            title = course.title,
            startDate = course.startDate,
            endDate = course.endDate
        )
    }

    private class DiffCallback : DiffUtil.ItemCallback<ClassScheduleAllClass>() {
        override fun areItemsTheSame(oldItem: ClassScheduleAllClass, newItem: ClassScheduleAllClass): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ClassScheduleAllClass, newItem: ClassScheduleAllClass): Boolean {
            return oldItem == newItem
        }
    }
}