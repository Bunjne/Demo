package whiz.tss.sspark.s_spark_android.presentation.learning_pathway.basic_course

import android.os.Bundle
import whiz.sspark.library.data.entity.Advisee
import whiz.sspark.library.data.entity.Course
import whiz.sspark.library.data.entity.Term
import whiz.sspark.library.extension.toJson
import whiz.sspark.library.extension.toObject

class AdviseeBasicCourseBottomSheetDialog: BasicCourseBottomSheetDialog() {
    companion object {
        fun newInstance(term: Term, courses: List<Course>, advisee: Advisee) = BasicCourseBottomSheetDialog().apply {
            arguments = Bundle().apply {
                putString("term", term.toJson())
                putString("courses", courses.toJson())
                putString("advisee", advisee.toJson())
            }
        }
    }

    private val advisee by lazy {
        arguments?.getString("term")?.toObject<Advisee>()!!
    }

    override fun initView() {
        super.initView()

        binding.vBasicCourse.showAdviseeProfile(advisee)
    }
}