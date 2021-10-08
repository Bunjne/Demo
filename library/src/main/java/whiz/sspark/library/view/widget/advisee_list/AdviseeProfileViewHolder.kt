package whiz.sspark.library.view.widget.advisee_list

import androidx.recyclerview.widget.RecyclerView
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.Advisee
import whiz.sspark.library.data.enum.getGender
import whiz.sspark.library.databinding.ViewAdviseeProfileItemViewBinding
import whiz.sspark.library.extension.showProfile

class AdviseeProfileViewHolder(
    private val binding: ViewAdviseeProfileItemViewBinding
): RecyclerView.ViewHolder(binding.root) {
    fun init(advisee: Advisee) {
        val context = binding.root.context
        with(advisee) {
            binding.ivProfile.showProfile(imageUrl, getGender(gender).type)
            binding.tvCodeAndNickname.text = context.resources.getString(R.string.advisee_list_code_and_name, code, nickname)
            binding.tvName.text = name
            binding.tvCredit.text = credit.toString()
            binding.tvTotalCredit.text = context.resources.getString(R.string.advisee_list_total_credit, totalCredit.toString())
            binding.tvGPA.text = gpa.toString()
        }
    }
}