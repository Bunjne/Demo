package whiz.sspark.library.view.today.happenings.news.news_small

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.data.entity.NewsDetail
import whiz.sspark.library.extension.toDP

class HappeningsNewsSmallAdapter(private val context: Context,
                                 private val newsDetail: List<NewsDetail>,
                                 private val onNewsClicked: (NewsDetail) -> Unit): RecyclerView.Adapter<HappeningsNewsSmallAdapter.ViewHolder>() {

    enum class TodayNewsAdapterViewType(val type: Int) {
        TEXT_ONLY(1),
        TEXT_IMAGE(2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == TodayNewsAdapterViewType.TEXT_IMAGE.type) {
            ViewHolder(HappeningsNewsSmallTextImageView(context).apply {
                layoutParams = RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.WRAP_CONTENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT
                )
            })
        } else {
            ViewHolder(HappeningsNewsSmallTextOnlyView(context).apply {
                layoutParams = RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.WRAP_CONTENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT
                )
            })
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = newsDetail.getOrNull(position)

        item?.let { item ->
            if (holder.itemViewType == TodayNewsAdapterViewType.TEXT_IMAGE.type) {
                (holder.itemView as? HappeningsNewsSmallTextImageView)?.init(item)
            } else {
                (holder.itemView as? HappeningsNewsSmallTextOnlyView)?.init(item)
            }

            when (position) {
                0 -> holder.itemView.setPadding(16.toDP(context), 0, 0, 0)
                newsDetail.lastIndex -> holder.itemView.setPadding(12.toDP(context), 0, 16.toDP(context), 0)
                else -> holder.itemView.setPadding(12.toDP(context), 0, 0, 0)
            }

            holder.itemView.setOnClickListener {
                onNewsClicked(item)
            }
        }
    }

    override fun getItemViewType(position: Int) =
        if (isNewsImageExisted(position)) {
            TodayNewsAdapterViewType.TEXT_IMAGE.type
        } else {
            TodayNewsAdapterViewType.TEXT_ONLY.type
        }


    override fun getItemCount() = newsDetail.size

    private fun isNewsImageExisted(position: Int) = newsDetail[position].coverImage.isNotBlank()

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

}