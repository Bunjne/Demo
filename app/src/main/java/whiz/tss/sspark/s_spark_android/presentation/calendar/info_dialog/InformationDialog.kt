package whiz.tss.sspark.s_spark_android.presentation.calendar.info_dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import whiz.sspark.library.data.entity.JuniorOutcome
import whiz.sspark.library.data.entity.SeniorOutcome
import whiz.sspark.library.data.entity.toInformationItems
import whiz.sspark.library.extension.setFullScreenWidth
import whiz.sspark.library.extension.toDP
import whiz.sspark.library.extension.toJson
import whiz.sspark.library.extension.toObjects
import whiz.sspark.library.view.general.information_dialog.InformationDialogAdapter
import whiz.tss.sspark.s_spark_android.R
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

        fun getOutcomesHeader(context: Context) = context.resources.getString(R.string.school_record_evaluation_title)

        fun getJuniorOutcomesItems(context: Context): List<InformationDialogAdapter.Item> = listOf(
            JuniorOutcome(0, context.resources.getString(R.string.school_record_zero_score_description_text)),
            JuniorOutcome(1, context.resources.getString(R.string.school_record_one_score_description_text)),
            JuniorOutcome(2, context.resources.getString(R.string.school_record_two_score_description_text)),
            JuniorOutcome(3, context.resources.getString(R.string.school_record_three_score_description_text)),
            JuniorOutcome(4, context.resources.getString(R.string.school_record_four_score_description_text)),
            JuniorOutcome(5, context.resources.getString(R.string.school_record_five_score_description_text)),
        ).toInformationItems()

        fun getSeniorOutcomeItems(context: Context): List<InformationDialogAdapter.Item> = listOf(
            SeniorOutcome(
                level = context.resources.getString(R.string.school_record_beginning_text),
                description = context.resources.getString(R.string.school_record_beginning_description_text)
            ),
            SeniorOutcome(
                level = context.resources.getString(R.string.school_record_developing_text),
                description = context.resources.getString(R.string.school_record_developing_description_text)
            ),
            SeniorOutcome(
                level = context.resources.getString(R.string.school_record_proficient_text),
                description = context.resources.getString(R.string.school_record_proficient_description_text)
            ),
            SeniorOutcome(
                level = context.resources.getString(R.string.school_record_advance_text),
                description = context.resources.getString(R.string.school_record_advance_description_text)
            )
        ).toInformationItems()
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
            it.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}