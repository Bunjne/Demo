package whiz.tss.sspark.s_spark_android.presentation.calendar.info_dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import whiz.sspark.library.extension.setFullScreenWidth
import whiz.sspark.library.extension.toDP
import whiz.sspark.library.extension.toJson
import whiz.sspark.library.extension.toObjects
import whiz.sspark.library.view.general.information_dialog.InformationDialogAdapter
import whiz.tss.sspark.s_spark_android.databinding.DialogInformationBinding

class InformationDialog: DialogFragment() {

    private var _binding: DialogInformationBinding? = null
    val binding get() = _binding!!

    companion object {
        fun newInstance(headerText: String,
                        informationItems: List<InformationDialogAdapter.Item>) = InformationDialog().apply {
            arguments = Bundle().apply {
                putString("headerText", headerText)
                putString("informationItems", informationItems.toJson())
            }
        }
    }

    private val informationItems: List<InformationDialogAdapter.Item>? by lazy {
        arguments?.getString("informationItems")?.toObjects(Array<InformationDialogAdapter.Item>::class.java)?.toList()
    }

    private val headerText: String by lazy {
        arguments?.getString("headerText") ?: ""
    }

    override fun onStart() {
        super.onStart()
        setFullScreenWidth(horizontalPadding = 31.toDP(requireContext()))
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogInformationBinding.inflate(LayoutInflater.from(context))
        val dialog = AlertDialog.Builder(requireContext()).create().apply {
            setView(binding.root)
            setCancelable(false)
            setDialogBackground(window)
        }
        initView(dialog)

        return dialog
    }

    private fun initView(dialog: AlertDialog) {
        informationItems?.let {
            binding.vInformationDialogView.init(
                headerText = headerText,
                items = it,
                onCloseClicked = {
                    dialog.dismiss()
                }
            )
        }
    }

    private fun setDialogBackground(window: Window?) {
        window?.let {
            it.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}