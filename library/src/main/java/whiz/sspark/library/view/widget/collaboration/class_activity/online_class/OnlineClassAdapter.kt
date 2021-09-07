package whiz.sspark.library.view.widget.collaboration.class_activity.online_class

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.PlatformOnlineClass
import whiz.sspark.library.extension.toDP

class OnlineClassAdapter(private val context: Context,
                         private val items: List<PlatformOnlineClass>,
                         private val onOnlineClassClicked: (String) -> Unit) : RecyclerView.Adapter<OnlineClassAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            OnlineClassItemView(context).apply {
                layoutParams = RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            })
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val platformOnlineClass = items.getOrNull(position)
        platformOnlineClass?.let {
            (holder.itemView as? OnlineClassItemView)?.apply {
                init(platformOnlineClass, onOnlineClassClicked)
                if (position == items.lastIndex) {
                    setPadding(0, 0, 0, 0)
                } else {
                    setPadding(0, 0, 16.toDP(context), 0)
                }
            }
        }
    }
}