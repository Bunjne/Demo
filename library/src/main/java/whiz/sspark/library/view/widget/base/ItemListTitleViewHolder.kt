package whiz.sspark.library.view.widget.base

import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.databinding.ViewItemListTitleBinding

class ItemListTitleViewHolder(
    private val binding: ViewItemListTitleBinding
): RecyclerView.ViewHolder(binding.root) {
    fun init(title: String) {
        binding.tvTitle.text = title
    }
}