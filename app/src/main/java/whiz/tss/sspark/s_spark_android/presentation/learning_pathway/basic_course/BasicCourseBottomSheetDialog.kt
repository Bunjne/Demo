package whiz.tss.sspark.s_spark_android.presentation.learning_pathway.basic_course

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import whiz.sspark.library.data.entity.Course
import whiz.sspark.library.data.entity.Term
import whiz.sspark.library.extension.toJson
import whiz.sspark.library.extension.toObject
import whiz.sspark.library.extension.toObjects
import whiz.tss.sspark.s_spark_android.databinding.FragmentBasicCourseBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseBottomSheetDialogFragment

open class BasicCourseBottomSheetDialog: BaseBottomSheetDialogFragment() {
    companion object {
        fun newInstance(term: Term, courses: List<Course>) = BasicCourseBottomSheetDialog().apply {
            arguments = Bundle().apply {
                putString("term", term.toJson())
                putString("courses", courses.toJson())
            }
        }
    }

    private var _binding: FragmentBasicCourseBinding? = null
    protected val binding get() = _binding!!

    private val term by lazy {
        arguments?.getString("term")?.toObject<Term>() ?: Term()
    }

    private val courses by lazy {
        arguments?.getString("courses")?.toObjects(Array<Course>::class.java) ?: listOf()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentBasicCourseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    override fun initView() {
        binding.vBasicCourse.init(
            term = term,
            courses = courses,
            onCloseClicked = {
                dismiss()
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}