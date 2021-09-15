package whiz.tss.sspark.s_spark_android.presentation.collaboration.class_post_comment.interaction

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import whiz.sspark.library.data.enum.PostInteraction
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.databinding.DialogLikeBySeenByBinding

class LikeBySeenByDialogFragment : DialogFragment() {

    companion object {
        fun newInstance(classGroupId: String, postId: String, startColor: Int, endColor: Int, postInteractionType: Int) = LikeBySeenByDialogFragment().apply {
            arguments = Bundle().apply {
                putString("classGroupId", classGroupId)
                putString("postId", postId)
                putInt("startColor", startColor)
                putInt("endColor", endColor)
                putInt("postInteractionType", postInteractionType)
            }
        }
    }

    private var _binding: DialogLikeBySeenByBinding? = null
    private val binding get() = _binding!!

    private val classGroupId by lazy {
        arguments?.getString("classGroupId") ?: ""
    }

    private val postId by lazy {
        arguments?.getString("postId") ?: ""
    }

    private val startColor by lazy {
        arguments?.getInt("startColor", ContextCompat.getColor(requireContext(), R.color.primaryStartColor)) ?: ContextCompat.getColor(requireContext(), R.color.primaryStartColor)
    }

    private val endColor by lazy {
        arguments?.getInt("endColor", ContextCompat.getColor(requireContext(), R.color.primaryEndColor)) ?: ContextCompat.getColor(requireContext(), R.color.primaryEndColor)
    }

    private val postInteractionType by lazy {
        arguments?.getInt("postInteractionType", PostInteraction.LIKE.type) ?: PostInteraction.LIKE.type
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            (resources.displayMetrics.heightPixels * 0.92).toInt()
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogLikeBySeenByBinding.inflate(LayoutInflater.from(context))
        val alertDialog = AlertDialog.Builder(context).create().apply {
            setView(binding.root)
            setCanceledOnTouchOutside(false)
            setCancelable(false)
            setDialogAnimation(window)
        }

//        alertDialog.dismiss()

        return alertDialog
    }

    private fun setDialogAnimation(window: Window?) {
        window?.let {
            with(window) {
                setBackgroundDrawableResource(R.drawable.bg_base_header_dialog)
                setGravity(Gravity.BOTTOM)
                attributes.windowAnimations = R.style.DialogAnimationStyle
            }
        }
    }
}