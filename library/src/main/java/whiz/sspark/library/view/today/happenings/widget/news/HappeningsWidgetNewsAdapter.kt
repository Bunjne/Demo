package whiz.sspark.library.view.today.happenings.widget.news

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.HighlightHeader
import whiz.sspark.library.data.entity.NewsDetail

class HappeningsWidgetNewsAdapter(private val context: Context,
                                  private val items: List<Item>,
                                  private val onNewsClicked: (NewsDetail) -> Unit,
                                  private val onSeeMoreClicked: (HighlightHeader) -> Unit): RecyclerView.Adapter<HappeningsWidgetNewsAdapter.ViewHolder>() {

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(HappeningsWidgetNewsView(context).apply {
            layoutParams = RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items.getOrNull(position)
        item?.let { news ->
            (holder.itemView as HappeningsWidgetNewsView).apply {
                init(news.header, news.newsList, news.latestPosition, onNewsClicked, onSeeMoreClicked) { position ->
                    news.latestPosition = position
                }
            }
        }
    }

    override fun getItemCount(): Int = items.size

    data class Item(
        val newsList: List<NewsDetail> = listOf(),
        val header: HighlightHeader = HighlightHeader(),
        var latestPosition: Int = 0
    )
}