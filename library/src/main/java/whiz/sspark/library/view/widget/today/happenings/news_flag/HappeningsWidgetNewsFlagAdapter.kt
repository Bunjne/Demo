package whiz.sspark.library.view.widget.today.happenings.news_flag

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.HighlightHeader
import whiz.sspark.library.data.entity.NewsDetail

class HappeningsWidgetNewsFlagAdapter(private val context: Context,
                                      private val items: List<Item>,
                                      private val onNewsClicked: (NewsDetail) -> Unit): RecyclerView.Adapter<HappeningsWidgetNewsFlagAdapter.ViewHolder>() {

    private var latestPosition = 0

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(HappeningsWidgetNewsFlagView(context).apply {
            layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        items.getOrNull(position)?.let {
            (holder.itemView as HappeningsWidgetNewsFlagView).apply {
                init(it.header, it.flagNews, latestPosition, onNewsClicked) { position ->
                    latestPosition = position
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return if (items.isNotEmpty()) {
            1
        } else {
            0
        }
    }

    data class Item(
        val flagNews: List<NewsDetail>,
        val header: HighlightHeader
    )

    fun setLatestPosition(position: Int){
        latestPosition = position
    }
}