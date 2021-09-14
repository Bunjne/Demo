package whiz.sspark.library.view.widget.collaboration.class_activity.online_class

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.PlatformOnlineClass
import whiz.sspark.library.extension.toDP

class OnlineClassAdapter(private val context: Context,
                         private val items: List<PlatformOnlineClass>,
                         private val onOnlineClassClicked: (String) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class OnlineClassItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemWidth = context.resources.displayMetrics.widthPixels - 140.toDP(context)
        return OnlineClassItemViewHolder(
            OnlineClassItemView(context).apply {
                layoutParams = RecyclerView.LayoutParams(
                    itemWidth,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            })
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
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