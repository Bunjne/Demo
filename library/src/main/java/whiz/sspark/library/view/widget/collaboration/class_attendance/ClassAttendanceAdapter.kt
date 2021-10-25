package whiz.sspark.library.view.widget.collaboration.class_attendance

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import whiz.sspark.library.data.entity.AttendanceDetail

class ClassAttendanceAdapter(private val context: Context,
                             private val attendanceDetails: List<AttendanceDetail>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ClassAttendanceItemViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ClassAttendanceItemViewHolder(ClassAttendanceItemView(context).apply {
            layoutParams = RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT
            )
        })
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val attendanceClass = attendanceDetails.getOrNull(position)
        attendanceClass?.let { attendanceClass ->
            (holder.itemView as? ClassAttendanceItemView)?.init(attendanceClass)
        }
    }

    override fun getItemCount() = attendanceDetails.size
}