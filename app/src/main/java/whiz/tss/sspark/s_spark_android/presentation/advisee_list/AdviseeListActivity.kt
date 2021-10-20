package whiz.tss.sspark.s_spark_android.presentation.advisee_list

import android.content.Intent
import android.os.Bundle
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.Advisee
import whiz.sspark.library.data.entity.AdviseeListStudentDTO
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.viewModel.AdviseeListViewModel
import whiz.sspark.library.extension.setGradientDrawable
import whiz.sspark.library.extension.toJson
import whiz.sspark.library.extension.toObject
import whiz.sspark.library.extension.toObjects
import whiz.sspark.library.utility.showAlertWithOkButton
import whiz.sspark.library.utility.showApiResponseXAlert
import whiz.sspark.library.view.widget.advisee_list.AdviseeListAdapter
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.databinding.ActivityAdviseeListBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseActivity
import whiz.tss.sspark.s_spark_android.presentation.menu.AdviseeMenuActivity

class AdviseeListActivity : BaseActivity() {

    companion object {
        const val MATHAYOM_4 = 10
        const val MATHAYOM_5 = 11
        const val MATHAYOM_6 = 12
    }

    private val viewModel: AdviseeListViewModel by viewModel()

    private lateinit var binding: ActivityAdviseeListBinding

    private val title by lazy {
        intent?.getStringExtra("title") ?: ""
    }

    private val segmentTitles by lazy {
        resources.getStringArray(R.array.advisee_list_segment).toList()
    }

    private var dataWrapper: DataWrapperX<Any>? = null
    private var advisees = listOf<AdviseeListStudentDTO>()

    private var isPrimaryHighSchool = true
    private var selectedSegment = -1
    private var currentSearchText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdviseeListBinding.inflate(layoutInflater)
        window.setGradientDrawable(R.drawable.bg_primary_gradient_0)
        setContentView(binding.root)

        initView()

        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState)
        }

        if (dataWrapper != null) {
            binding.vAdviseeList.setInitialHeader(selectedSegment, currentSearchText)
            binding.vAdviseeList.updateAdapterHeader(isPrimaryHighSchool)
            binding.vAdviseeList.setLatestUpdatedText(dataWrapper)
            updateAdapterItem()
        } else {
            setNoAdvisee()
            binding.vAdviseeList.updateAdapterHeader(isPrimaryHighSchool)
            viewModel.getAdviseeList()
        }
    }

    override fun initView() {
        binding.vAdviseeList.init(
            title = title,
            segmentTitles = segmentTitles,
            onSegmentClicked = {
                selectedSegment = it
                updateAdapterItem()
            },
            onAdviseeClicked = {
                val intent = Intent(this, AdviseeMenuActivity::class.java).apply {
                    putExtra("advisee", it.toJson())
                    putExtra("studentId", it.id)
                }
                startActivity(intent)
            },
            onTextChanged = {
                currentSearchText = it
                updateAdapterItem()
            },
            onRefresh = {
                viewModel.getAdviseeList()
            }
        )
    }

    override fun observeView() {
        viewModel.viewLoading.observe(this) { isLoading ->
            binding.vAdviseeList.setSwipeRefreshLayout(isLoading)
        }

        viewModel.viewRendering.observe(this) {
            dataWrapper = it
            binding.vAdviseeList.setLatestUpdatedText(it)
        }
    }

    override fun observeData() {
        viewModel.adviseeListResponse.observe(this) {
            it?.let {
                if (isPrimaryHighSchool != it.isJunior) {
                    isPrimaryHighSchool = it.isJunior
                    binding.vAdviseeList.updateAdapterHeader(isPrimaryHighSchool)
                }

                advisees = it.students
                updateAdapterItem()
            }
        }
    }

    override fun observeError() {
        viewModel.adviseeListErrorResponse.observe(this) {
            it?.let {
                showApiResponseXAlert(this, it)
            }
        }

        viewModel.errorMessage.observe(this) {
            it?.let {
                showAlertWithOkButton(it)
            }
        }
    }

    private fun updateAdapterItem() {
        val filteredAdvisees = advisees.filter {
            val isSearchTextMatched  = if (currentSearchText.isNotEmpty()) {
                it.fullNameEn.contains(currentSearchText, true) ||
                it.fullNameTh.contains(currentSearchText, true) ||
                it._nickNameEn.contains(currentSearchText, true) ||
                it._nickNameTh.contains(currentSearchText, true) ||
                it.code.contains(currentSearchText, true) ||
                it.number?.toString()?.contains(currentSearchText, true) == true
            } else {
                true
            }

            val isSelectedAcademicGradeExisted = if (!isPrimaryHighSchool) {
                when (selectedSegment) {
                    1 -> it.term.academicGrade == MATHAYOM_4
                    2 -> it.term.academicGrade == MATHAYOM_5
                    3 -> it.term.academicGrade == MATHAYOM_6
                    else -> true
                }
            } else {
                true
            }

            isSearchTextMatched && isSelectedAcademicGradeExisted
        }

        if (filteredAdvisees.isNotEmpty()) {
            val convertedAdvisees = filteredAdvisees.map {
                val studentCode = if (isPrimaryHighSchool) {
                    it.number?.toString() ?: "0"
                } else {
                    it.code
                }

                val advisee = Advisee(
                    id = it.id,
                    nickname = it.nickName,
                    code = studentCode,
                    name = it.fullName,
                    imageUrl = it.imageUrl,
                    gender = it.gender,
                    gpa = it.gpa,
                    credit = it.credit,
                    totalCredit = it.totalCredit
                )

                AdviseeListAdapter.AdviseeListItem.Student(advisee)
            }

            binding.vAdviseeList.updateItem(convertedAdvisees)
        } else {
            setNoAdvisee()
        }
    }

    private fun setNoAdvisee() {
        val noAdviseeItem = AdviseeListAdapter.AdviseeListItem.NoAdvisee(resources.getString(R.string.advisee_list_no_advisee))
        binding.vAdviseeList.updateItem(listOf(noAdviseeItem))
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        dataWrapper = savedInstanceState.getString("dataWrapper")?.toObject<DataWrapperX<Any>>()
        advisees = savedInstanceState.getString("advisees")?.toObjects(Array<AdviseeListStudentDTO>::class.java) ?: listOf()
        currentSearchText = savedInstanceState.getString("currentSearchText") ?: ""
        isPrimaryHighSchool = savedInstanceState.getBoolean("isPrimaryHighSchool", true)
        selectedSegment = savedInstanceState.getInt("selectedSegment", 0)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("dataWrapper", dataWrapper?.toJson())
        outState.putString("advisees", advisees.toJson())
        outState.putString("currentSearchText", currentSearchText)
        outState.putBoolean("isPrimaryHighSchool", isPrimaryHighSchool)
        outState.putInt("selectedSegment", selectedSegment)
    }
}