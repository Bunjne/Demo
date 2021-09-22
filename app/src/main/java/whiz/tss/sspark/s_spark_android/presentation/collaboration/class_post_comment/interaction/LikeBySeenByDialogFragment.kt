package whiz.tss.sspark.s_spark_android.presentation.collaboration.class_post_comment.interaction

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.Member
import whiz.sspark.library.data.enum.PostInteraction
import whiz.sspark.library.data.viewModel.LikeBySeenByViewModel
import whiz.sspark.library.utility.showAlertWithOkButton
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.sspark.library.view.widget.collaboration.class_post_comment.interaction.LikeBySeenByItemAdapter
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

    private val viewModel: LikeBySeenByViewModel by viewModel()

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
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogLikeBySeenByBinding.inflate(layoutInflater)

        val alertDialog = AlertDialog.Builder(requireContext()).create().apply {
            setView(binding.root)
            setCancelable(false)
            setDialogAnimation(window)
        }

        binding.vLikeBySeenBy.init(
            startColor = startColor,
            endColor = endColor,
            postInteractionType = postInteractionType,
            onCloseClicked = {
                alertDialog.dismiss()
            },
            onRefresh = {
                when (postInteractionType) {
                    PostInteraction.LIKE.type -> viewModel.getUserIdsByPostLiked(postId)
                    PostInteraction.SEEN.type -> viewModel.getUserIdsByPostSeen(postId)
                }
            }
        )

        return alertDialog
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.let {
            observeView()
            observeData()
            observeError()
        }

        when (postInteractionType) {
            PostInteraction.LIKE.type -> viewModel.getUserIdsByPostLiked(postId)
            PostInteraction.SEEN.type -> viewModel.getUserIdsByPostSeen(postId)
        }
    }

    private fun setDialogAnimation(window: Window?) {
        window?.let {
            with(window) {
                decorView.setBackgroundColor(Color.TRANSPARENT)
                setGravity(Gravity.BOTTOM)
                attributes.windowAnimations = R.style.DialogAnimationStyle
            }
        }
    }

    private fun observeView() {
        viewModel.viewLoading.observe(this) { isLoading ->
            binding.vLikeBySeenBy.setSwipeRefreshLoading(isLoading)
        }
    }

    private fun observeData() {
        viewModel.userIdsResponse.observe(this) {
              viewModel.getMember(classGroupId)
        }

        viewModel.memberResponses.observe(this) { member ->
            viewModel.userIdsResponse.value?.let { userIds ->
                binding.vLikeBySeenBy.renderMembers(
                    matchedMembers = filterMember(
                        allMembers = member,
                        interactedMemberIds = userIds
                    )
                )
            }
        }
    }

    private fun observeError() {
        with(viewModel) {
            listOf(userIdsErrorResponse, memberErrorResponse).forEach {
                it.observe(this@LikeBySeenByDialogFragment) {
                    it?.let {
                        showApiResponseXAlert(activity, it)
                    }
                }
            }

            errorMessage.observe(this@LikeBySeenByDialogFragment) {
                it?.let {
                    requireContext().showAlertWithOkButton(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun filterMember(allMembers: Member, interactedMemberIds: List<String>): MutableList<LikeBySeenByItemAdapter.LikeBySeenByAdapterViewType> {
        val instructors = allMembers.instructors.filter { interactedMemberIds.contains(it.userId) }
        val students = allMembers.students.filter { interactedMemberIds.contains(it.userId) }
        val filteredMember = mutableListOf<LikeBySeenByItemAdapter.LikeBySeenByAdapterViewType>()

        with(filteredMember) {
            if (instructors.isNotEmpty()) {
                add(
                    LikeBySeenByItemAdapter.LikeBySeenByAdapterViewType.Header(
                        title = requireContext().resources.getString(whiz.sspark.library.R.string.like_by_seen_by_instructor_title, instructors.size)
                    )
                )

                addAll(
                    instructors.map { instructor ->
                        LikeBySeenByItemAdapter.LikeBySeenByAdapterViewType.Instructor(
                            instructor = instructor)
                    }
                )
            }

            if (students.isNotEmpty()) {
                add(
                    LikeBySeenByItemAdapter.LikeBySeenByAdapterViewType.Header(
                        title = requireContext().resources.getString(whiz.sspark.library.R.string.like_by_seen_by_student_title, students.size)
                    )
                )

                addAll(
                    students.mapIndexed { index, student ->
                        LikeBySeenByItemAdapter.LikeBySeenByAdapterViewType.Student(
                            student = student,
                            rank = index + 1)
                    }
                )
            }
        }

        return filteredMember
    }
}