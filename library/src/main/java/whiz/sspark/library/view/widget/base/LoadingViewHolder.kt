package whiz.sspark.library.view.widget.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.databinding.ViewLoadingBinding

class LoadingViewHolder(
    private val binding: ViewLoadingBinding
):RecyclerView.ViewHolder(binding.root) {
    fun init(isShowing: Boolean) {
        if (isShowing) {
            binding.pbLoading.visibility = View.VISIBLE
        } else{
            binding.pbLoading.visibility = View.GONE
        }
    }
}