package whiz.sspark.library.utility

import androidx.recyclerview.widget.RecyclerView

fun <T> RecyclerView.Adapter<RecyclerView.ViewHolder>.updateItem(item: MutableList<T>, updateItem: List<T>) {
    val oldItemCount = item.size
    item.clear()
    when {
        oldItemCount > 1 -> this.notifyItemRangeChanged(0, oldItemCount)
        oldItemCount == 1 -> this.notifyItemRemoved(0)
    }

    item.addAll(updateItem)
    val newItemCount = item.size
    when {
        newItemCount > 1 -> this.notifyItemRangeChanged(0, newItemCount)
        newItemCount == 1 -> this.notifyItemChanged(0)
    }
}