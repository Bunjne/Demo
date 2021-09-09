package whiz.sspark.library.view.widget.today.happenings.news_flag

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.NewsDetail
import whiz.sspark.library.extension.toDP

class HappeningsNewsFlagAdapter(private val context: Context,
                                private val newsDetail: List<NewsDetail>,
                                private val onNewsClicked: (NewsDetail) -> Unit): RecyclerView.Adapter<HappeningsNewsFlagAdapter.ViewHolder>() {

    private val defaultBorderPadding = 16.toDP(context)
    private val defaultItemPadding = 16.toDP(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(HappeningsNewsFlagView(context).apply {
            layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.MATCH_PARENT
            )
        })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = newsDetail.getOrNull(position)

        item?.let { item ->
            (holder.itemView as? HappeningsNewsFlagView)?.init(item, onNewsClicked)

            holder.itemView.setPadding(defaultItemPadding, 0, defaultBorderPadding, 0)
        }
    }

    override fun getItemCount() = newsDetail.size

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

}