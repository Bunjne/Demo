package whiz.sspark.library.view.widget.base

import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.databinding.ViewCenterTextBinding

class CenterTextViewHolder(
    private val binding: ViewCenterTextBinding
):RecyclerView.ViewHolder(binding.root) {
    fun init(title: String) {
        binding.tvTitle.text = title
    }
}