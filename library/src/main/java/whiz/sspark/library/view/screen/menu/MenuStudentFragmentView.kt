package whiz.sspark.library.view.screen.menu

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.*
import whiz.sspark.library.data.enum.MenuSegmentType
import whiz.sspark.library.data.enum.getGender
import whiz.sspark.library.databinding.ViewMenuStudentFragmentBinding
import whiz.sspark.library.extension.show
import whiz.sspark.library.extension.showUserProfileCircle
import whiz.sspark.library.view.general.CustomDividerMultiItemDecoration
import whiz.sspark.library.view.widget.menu.MenuSegmentAdapter
import whiz.sspark.library.view.widget.menu.MenuMemberAdapter

class MenuStudentFragmentView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewMenuStudentFragmentBinding.inflate(LayoutInflater.from(context), this, true)
    }
    private val config by lazy {
        ConcatAdapter.Config.Builder().apply {
            setIsolateViewTypes(false)
        }.build()
    }

    private var menuSegmentType = MenuSegmentType.INSTRUCTOR.type

    private val concatAdapter: ConcatAdapter? = null
    private var segmentAdapter: MenuSegmentAdapter? = null
    private var memberAdapter: MenuMemberAdapter? = null

    private val members: MutableList<MenuMember> = mutableListOf()

    fun init(student: Student,
             segments: List<MenuSegment>,
             onCameraClicked: () -> Unit,
             onMemberClicked: (MenuMember) -> Unit,
             onRefresh: () -> Unit) {

        val convertedMember = student.getMenuMember(context)
        with(this.members) {
            clear()
            addAll(convertedMember)
        }

        binding.ivCamera.show(R.drawable.ic_camera)
        binding.vGradientTop.show(R.drawable.bg_primary_gradient_0)
        binding.ivProfile.showUserProfileCircle(student.profileImageUrl, getGender(student.gender).type)
        binding.tvFirstname.text = student.firstName
        binding.tvLastname.text = student.lastName
        binding.tvIDCardNumber.text = resources.getString(R.string.menu_id_card_number, student.code)
        binding.cvCamera.setOnClickListener {
            onCameraClicked()
        }

        binding.srlMenu.setOnRefreshListener {
            resetMenuSegment()
            onRefresh()
        }

        segmentAdapter = MenuSegmentAdapter(
            context = context,
            segments = segments,
            onSegmentSelected = {
                menuSegmentType = it
                updateMemberAdapter()
            }
        )

        memberAdapter = MenuMemberAdapter(context, onMemberClicked)

        val layoutManager = GridLayoutManager(context, 12)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (concatAdapter?.getItemViewType(position)) {
                    MenuSegmentAdapter.VIEW_TYPE -> 12
                    MenuMemberAdapter.VIEW_TYPE -> 12
                    else -> 12
                }
            }
        }

        with(binding.rvMenu) {
            if (itemDecorationCount == 0) {
                addItemDecoration(CustomDividerMultiItemDecoration(
                    divider = ContextCompat.getDrawable(context, R.drawable.divider_list_base)!!,
                    dividerViewType = listOf(MenuMemberAdapter.VIEW_TYPE)
                ))
            }

            this.layoutManager = layoutManager
            adapter = ConcatAdapter(config, segmentAdapter, memberAdapter)
        }

        updateMemberAdapter()
    }

    private fun resetMenuSegment() {
        menuSegmentType = MenuSegmentType.INSTRUCTOR.type
        updateMemberAdapter()
        segmentAdapter?.resetSelectedTab()
    }

    private fun updateMemberAdapter() {
        val filterMember = members.filter { it.type.type == menuSegmentType }
        memberAdapter?.submitList(filterMember)
    }

    fun updateStudentProfileImage(profileImageUrl: String, gender: Long) {
        binding.ivProfile.showUserProfileCircle(profileImageUrl, gender)
    }
}