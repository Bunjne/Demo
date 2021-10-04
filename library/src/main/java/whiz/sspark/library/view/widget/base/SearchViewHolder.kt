package whiz.sspark.library.view.widget.base

import android.text.TextWatcher
import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.R
import whiz.sspark.library.databinding.ViewSearchItemViewBinding
import whiz.sspark.library.extension.show

class SearchViewHolder(
    private val binding: ViewSearchItemViewBinding
): RecyclerView.ViewHolder(binding.root) {
    fun init(watcher: TextWatcher) {
        binding.ivSearch.show(R.drawable.ic_search)
        binding.etSearch.removeTextChangedListener(watcher)
        binding.etSearch.addTextChangedListener(watcher)
    }

    fun setText(currentText: String) {
        binding.etSearch.setText(currentText)
    }
}