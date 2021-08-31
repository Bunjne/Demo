package whiz.sspark.library.view.screen.menu

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.MenuMember
import whiz.sspark.library.data.entity.MenuSegment
import whiz.sspark.library.data.entity.Student
import whiz.sspark.library.data.enum.MenuSegmentType
import whiz.sspark.library.data.enum.getGender
import whiz.sspark.library.databinding.ViewMenuStudentFragmentBinding
import whiz.sspark.library.extension.show
import whiz.sspark.library.extension.showUserProfileCircle
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

    private val member: MutableList<MenuMember> = mutableListOf()
    private val filteredMember: MutableList<MenuMember> = mutableListOf()

    fun init(student: Student,
             segments: List<MenuSegment>,
             onCameraClicked: () -> Unit,
             onMemberClicked: (MenuMember) -> Unit,
             onRefresh: () -> Unit) {

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
            clearData()
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

        memberAdapter = MenuMemberAdapter(context, filteredMember, onMemberClicked)

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
            this.layoutManager = layoutManager
            adapter = ConcatAdapter(config, segmentAdapter, memberAdapter)
        }
    }

    private fun clearData() {
        member.clear()
        menuSegmentType = MenuSegmentType.INSTRUCTOR.type
        updateMemberAdapter()
        segmentAdapter?.resetSelectedTab()
    }

    fun updateMember(members: List<MenuMember>) {
        member.clear()
        member.addAll(members)

        updateMemberAdapter()

        binding.srlMenu.isRefreshing = false
    }

    private fun updateMemberAdapter() {
        val filterMember = member.filter { it.type.type == menuSegmentType }

        val oldItemCount = filteredMember.size
        filteredMember.clear()
        when {
            oldItemCount > 1 -> memberAdapter?.notifyItemRangeRemoved(0, oldItemCount)
            oldItemCount == 1 -> memberAdapter?.notifyItemRemoved(0)
        }

        filteredMember.addAll(filterMember)
        val newItemCount = filteredMember.size
        when {
            newItemCount > 1 -> memberAdapter?.notifyItemRangeInserted(0, newItemCount)
            newItemCount == 1 -> memberAdapter?.notifyItemInserted(0)
        }
    }

    fun updateStudentProfileImage(profileImageUrl: String, gender: Long) {
        binding.ivProfile.showUserProfileCircle(profileImageUrl, gender)
    }
}