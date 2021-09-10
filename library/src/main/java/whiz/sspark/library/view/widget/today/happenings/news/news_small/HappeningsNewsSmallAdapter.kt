package whiz.sspark.library.view.widget.today.happenings.news.news_small

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.NewsDetail
import whiz.sspark.library.extension.toDP

class HappeningsNewsSmallAdapter(private val context: Context,
                                 private val newsDetail: List<NewsDetail>,
                                 private val onNewsClicked: (NewsDetail) -> Unit): RecyclerView.Adapter<HappeningsNewsSmallAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(HappeningsNewsSmallTextImageView(context).apply {
                layoutParams = RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.WRAP_CONTENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT
                )
        })
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = newsDetail.getOrNull(position)

        item?.let { item ->
            (holder.itemView as? HappeningsNewsSmallTextImageView)?.init(item)

            when (position) {
                0 -> holder.itemView.setPadding(8.toDP(context), 0, 0, 0)
                newsDetail.lastIndex -> holder.itemView.setPadding(8.toDP(context), 0, 8.toDP(context), 0)
                else -> holder.itemView.setPadding(8.toDP(context), 0, 0, 0)
            }

            holder.itemView.setOnClickListener {
                onNewsClicked(item)
            }
        }
    }

    override fun getItemCount() = newsDetail.size

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

}