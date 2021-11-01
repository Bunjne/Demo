package whiz.sspark.library.view.widget.base

import android.content.res.ColorStateList
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.databinding.ViewLoadingBinding

class LoadingViewHolder(
    private val binding: ViewLoadingBinding
):RecyclerView.ViewHolder(binding.root) {
    fun init(isShowing: Boolean, color: Int) {
        if (isShowing) {
            binding.pbLoading.indeterminateTintList = ColorStateList.valueOf(color)
            binding.pbLoading.visibility = View.VISIBLE
        } else{
            binding.pbLoading.visibility = View.GONE
        }
    }
}