package whiz.sspark.library.view.widget.base

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.databinding.ViewItemListTitleBinding

class ItemListTitleViewHolder(private val binding: ViewItemListTitleBinding): RecyclerView.ViewHolder(binding.root) {
    fun init(title: String) {
        binding.tvTitle.text = title
    }
}