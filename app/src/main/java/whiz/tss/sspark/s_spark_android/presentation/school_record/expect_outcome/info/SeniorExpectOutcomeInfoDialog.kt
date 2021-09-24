package whiz.tss.sspark.s_spark_android.presentation.school_record.expect_outcome.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import whiz.sspark.library.extension.show
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.databinding.FragmentSeniorExpectOutcomeInfoBinding

class SeniorExpectOutcomeInfoDialog: DialogFragment() {
    companion object {
        fun newInstance() = SeniorExpectOutcomeInfoDialog()
    }

    private var _binding: FragmentSeniorExpectOutcomeInfoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSeniorExpectOutcomeInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
    }

    private fun initView() {
        binding.ivClose.show(R.drawable.ic_cancel)
        binding.ivClose.setOnClickListener {
            dialog?.dismiss()
        }
    }

    override fun onStart() {
        super.onStart()
        val width = resources.displayMetrics.widthPixels * 8 / 10
        dialog?.window?.setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawable(ContextCompat.getDrawable(requireContext(), R.color.transparentColor))
    }

    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}