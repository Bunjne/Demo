package whiz.tss.sspark.s_spark_android.presentation.contact

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import whiz.sspark.library.data.entity.Contact
import whiz.sspark.library.data.entity.ContactInfo
import whiz.sspark.library.extension.toDP
import whiz.sspark.library.extension.toObject
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.databinding.DialogContactInfoBinding

class ContactInfoBottomSheetDialog : BottomSheetDialogFragment() {
    companion object {
        fun newInstance(contactInfo: String) = ContactInfoBottomSheetDialog().apply {
            arguments = Bundle().apply {
                putString("contactInfo", contactInfo)
            }
        }
    }

    private var _binding: DialogContactInfoBinding? = null
    private val binding get() = _binding!!

    private val contactInfo by lazy {
        arguments?.getString("contactInfo")?.toObject<Contact>() ?: Contact()
    }

    private var listener: DialogOnClickListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = (context as? DialogOnClickListener)
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DialogContactInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()

        dialog?.setOnShowListener {
            validateBottomSheet()
        }
    }

    fun initView() {
        binding.vContactInfo.init(
            contact = contactInfo,
            onContactInfoClicked = {
                listener?.onContactInfoSelected(it)
            },
            onClosed = {
                dismiss()
            }
        )
    }

    private fun validateBottomSheet() {
        dialog?.setCanceledOnTouchOutside(false)

        val bottomSheetDialog = dialog as? BottomSheetDialog
        bottomSheetDialog?.let {
            val bottomSheet = dialog!!.findViewById<FrameLayout>(R.id.design_bottom_sheet)
            bottomSheet?.apply {
                val params = layoutParams
                params.height = WindowManager.LayoutParams.WRAP_CONTENT
                layoutParams = params
                setBackgroundColor(Color.TRANSPARENT)
                setPadding(0,12.toDP(requireContext()),0, 12.toDP(requireContext()))
            }

            BottomSheetBehavior.from(bottomSheet).run {
                state = BottomSheetBehavior.STATE_EXPANDED

                addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onSlide(bottomSheet: View, slideOffset: Float) { }

                    override fun onStateChanged(bottomSheet: View, newState: Int) {
                        if (state == BottomSheetBehavior.STATE_COLLAPSED) {
                            dismiss()
                        }
                    }
                })
            }
        }
    }

    interface DialogOnClickListener {
        fun onContactInfoSelected(contactInfo: ContactInfo)
    }
}