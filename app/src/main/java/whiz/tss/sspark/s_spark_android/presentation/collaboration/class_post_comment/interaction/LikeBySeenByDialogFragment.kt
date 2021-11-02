package whiz.tss.sspark.s_spark_android.presentation.collaboration.class_post_comment.interaction

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.LikeBySeenByMember
import whiz.sspark.library.data.entity.Member
import whiz.sspark.library.data.enum.PostInteraction
import whiz.sspark.library.data.viewModel.LikeBySeenByViewModel
import whiz.sspark.library.extension.getFirstConsonant
import whiz.sspark.library.extension.toColor
import whiz.sspark.library.utility.convertToFullName
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

    private val items = mutableListOf<LikeBySeenByItemAdapter.LikeBySeenByAdapterViewType>()

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
            matchedMembers = items,
            startColor = startColor,
            endColor = endColor,
            postInteractionType = postInteractionType,
            onCloseClicked = {
                alertDialog.dismiss()
            },
            onRefresh = {
                when (postInteractionType) {
                    PostInteraction.LIKE.type -> viewModel.getMembersByPostLiked(classGroupId, postId)
                    PostInteraction.SEEN.type -> viewModel.getMembersByPostSeen(classGroupId, postId)
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
            PostInteraction.LIKE.type -> viewModel.getMembersByPostLiked(classGroupId, postId)
            PostInteraction.SEEN.type -> viewModel.getMembersByPostSeen(classGroupId, postId)
        }
    }

    private fun setDialogAnimation(window: Window?) {
        window?.let {
            with(window) {
                decorView.setBackgroundColor(Color.TRANSPARENT)
                setGravity(Gravity.BOTTOM)
                attributes.windowAnimations = R.style.VerticalSlidesAnimationStyle
            }
        }
    }

    private fun observeView() {
        viewModel.viewLoading.observe(this) { isLoading ->
            binding.vLikeBySeenBy.setSwipeRefreshLoading(isLoading)
        }
    }

    private fun observeData() {
        viewModel.membersResponse.observe(this) { member ->
            val matchedMembers = filterMembers(member)
            binding.vLikeBySeenBy.renderMembers(items, matchedMembers)
        }
    }

    private fun observeError() {
        with(viewModel) {
            viewModel.memberErrorResponse.observe(this@LikeBySeenByDialogFragment) {
                it?.let {
                    showApiResponseXAlert(activity, it) {
                        dismiss()
                    }
                }
            }

            viewModel.errorMessage.observe(this@LikeBySeenByDialogFragment) {
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

    private fun filterMembers(allMembers: Member): MutableList<LikeBySeenByItemAdapter.LikeBySeenByAdapterViewType> {
        val instructors = allMembers.instructors
        val students = allMembers.students
        val filteredMembers = mutableListOf<LikeBySeenByItemAdapter.LikeBySeenByAdapterViewType>()

        with(filteredMembers) {
            if (instructors.isNotEmpty()) {
                add(
                    LikeBySeenByItemAdapter.LikeBySeenByAdapterViewType.Header(
                        title = requireContext().resources.getString(R.string.like_by_seen_by_instructor_title, instructors.size)
                    )
                )

                addAll(
                    instructors.map { instructor ->
                        with(instructor) {
                            LikeBySeenByItemAdapter.LikeBySeenByAdapterViewType.Instructor(
                                instructor = LikeBySeenByMember(
                                    profileImageUrl = imageUrl,
                                    color = colorCode?.toColor() ?: Color.BLACK,
                                    fullName = convertToFullName(firstName, middleName, lastName),
                                    jobDescription = jobDescription,
                                    gender = gender
                                )
                            )
                        }
                    }
                )
            }

            if (students.isNotEmpty()) {
                add(
                    LikeBySeenByItemAdapter.LikeBySeenByAdapterViewType.Header(
                        title = requireContext().resources.getString(R.string.like_by_seen_by_student_title, students.size)
                    )
                )

                addAll(
                    students.map { student ->
                        with(student) {
                            val title = resources.getString(R.string.like_by_seen_by_name_with_rank, number ?: code, collaborationDisplayName)

                            LikeBySeenByItemAdapter.LikeBySeenByAdapterViewType.Student(
                                student = LikeBySeenByMember(
                                    title = title,
                                    profileImageUrl = imageUrl,
                                    color = colorCode?.toColor() ?: Color.BLACK,
                                    fullName = convertToFullName(firstName, middleName, lastName),
                                    gender = gender
                                )
                            )
                        }
                    }
                )
            }
        }

        return filteredMembers
    }
}