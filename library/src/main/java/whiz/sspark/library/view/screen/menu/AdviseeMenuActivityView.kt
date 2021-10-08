package whiz.sspark.library.view.screen.menu

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.Advisee
import whiz.sspark.library.data.entity.MenuMemberItem
import whiz.sspark.library.data.enum.getGender
import whiz.sspark.library.databinding.ViewAdviseeMenuActivityBinding
import whiz.sspark.library.extension.showProfile
import whiz.sspark.library.view.general.custom_divider.CustomDividerMultiItemDecoration
import whiz.sspark.library.view.widget.menu.AdviseeGuardianAdapter
import whiz.sspark.library.view.widget.menu.MenuAdapter
import java.lang.Exception

class AdviseeMenuActivityView: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewAdviseeMenuActivityBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private var guardianAdapter: AdviseeGuardianAdapter? = null
    private var menuAdapter: MenuAdapter? = null

    fun init(advisee: Advisee?,
             onMemberClicked: (MenuMemberItem) -> Unit,
             onMenuClicked: (String) -> Unit,
             onRefresh: () -> Unit) {
        updateAdviseeInfo(advisee)

        guardianAdapter = AdviseeGuardianAdapter(
            onMemberClicked = onMemberClicked
        )

        menuAdapter = MenuAdapter(
            context = context,
            onMenuClicked = onMenuClicked
        )

        val config = ConcatAdapter.Config.Builder().apply {
            setIsolateViewTypes(false)
        }.build()

        val concatAdapter = ConcatAdapter(config, guardianAdapter, menuAdapter)

        with(binding.rvMenu) {
            if (itemDecorationCount == 0) {
                addItemDecoration(
                    CustomDividerMultiItemDecoration(
                        divider = ContextCompat.getDrawable(context, R.drawable.divider_list_base)!!,
                        dividerViewType = listOf(
                            AdviseeGuardianAdapter.GUARDIAN_VIEW_TYPE,
                            MenuAdapter.MENU_TYPE
                        )
                    )
                )
            }

            layoutManager = LinearLayoutManager(context)
            adapter = concatAdapter
        }

        binding.srlContainer.setOnRefreshListener(onRefresh)
    }

    fun setSwipeRefreshLayout(isLoading: Boolean) {
        binding.srlContainer.isRefreshing = isLoading == true
    }

    fun updateAdviseeInfo(advisee: Advisee?) {
        if (advisee != null) {
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

    fun updateGuardian(items: List<AdviseeGuardianAdapter.AdviseeGuardianItem>) {
        guardianAdapter?.submitList(items) {
            try {
                binding.rvMenu.scrollToPosition(0)
            } catch (e: Exception) { }
        }
    }

    fun updateMenu(items: List<MenuAdapter.Item>) {
        menuAdapter?.submitList(items) {
            try {
                binding.rvMenu.scrollToPosition(0)
            } catch (e: Exception) { }
        }
    }
}