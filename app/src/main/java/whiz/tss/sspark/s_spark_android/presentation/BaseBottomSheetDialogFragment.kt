package whiz.tss.sspark.s_spark_android.presentation

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import whiz.sspark.library.data.entity.Profile
import whiz.sspark.library.view.general.loading_dialog.SSparkLoadingDialog
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.utility.ProfileManager

abstract class BaseBottomSheetDialogFragment : BottomSheetDialogFragment() {

    protected val profileManager by lazy {
        ProfileManager(requireContext())
    }

    protected val loadingDialog by lazy {
        SSparkLoadingDialog(requireContext())
    }

    protected var profile: Profile? = null

    open val isForceFullScreen = false
    open val isDraggable = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            profileManager.profile.collect {
                it?.let {
                    profile = it
                }
            }
        }

        observeView()
        observeData()
        observeError()

        dialog?.setOnShowListener {
            validateDialog()
        }
    }

    private fun validateDialog() {
        val bottomSheetDialog = dialog as? BottomSheetDialog
        bottomSheetDialog?.let {
            val bottomSheet = dialog!!.findViewById<FrameLayout>(R.id.design_bottom_sheet)
            bottomSheet.setBackgroundColor(Color.TRANSPARENT)
            BottomSheetBehavior.from(bottomSheet).run {

                isDraggable = this@BaseBottomSheetDialogFragment.isDraggable
                state = BottomSheetBehavior.STATE_EXPANDED

                addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onSlide(p0: View, p1: Float) { }

                    override fun onStateChanged(bottomSheet: View, state: Int) {
                        if (state == BottomSheetBehavior.STATE_COLLAPSED) {
                            dismiss()
                        }
                    }
                })
            }

            if (isForceFullScreen) {
                val layoutParams = bottomSheet.layoutParams

                val windowHeight: Int = resources.displayMetrics.heightPixels
                if (layoutParams != null) {
                    layoutParams.height = windowHeight
                }

                bottomSheet.layoutParams = layoutParams
            }

            view?.requestLayout()
        }
    }

    open fun initView() { }
    open fun observeView() { }
    open fun observeData() { }
    open fun observeError() { }
}