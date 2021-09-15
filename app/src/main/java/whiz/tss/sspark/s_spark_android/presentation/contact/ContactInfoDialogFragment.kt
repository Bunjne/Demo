package whiz.tss.sspark.s_spark_android.presentation.contact

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import whiz.sspark.library.data.entity.Contact
import whiz.sspark.library.data.entity.ContactInfo
import whiz.sspark.library.extension.toObject
import whiz.tss.sspark.s_spark_android.R
import whiz.tss.sspark.s_spark_android.databinding.DialogContactInfoBinding

class ContactInfoDialogFragment : DialogFragment() {
    companion object {
        fun newInstance(contactInfo: String) = ContactInfoDialogFragment().apply {
            arguments = Bundle().apply {
                putString("contactInfo", contactInfo)
            }
        }
    }

    private var _binding: DialogContactInfoBinding?= null
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogContactInfoBinding.inflate(LayoutInflater.from(requireContext()), null, false)

        val alertDialog = AlertDialog.Builder(context).create().apply {
            setView(binding.root)
            setCanceledOnTouchOutside(false)
            setCancelable(false)
            setDialogAnimation(window)
            window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }

        binding.vContactInfo.init(
            contact = contactInfo,
            onContactInfoClicked = {
                listener?.onContactInfoSelected(it)
            },
            onClosed = {
                alertDialog.dismiss()
            }
        )

        return alertDialog
    }

    private fun setDialogAnimation(window: Window?) {
        window?.let {
            with(window) {
                decorView.setBackgroundColor(Color.TRANSPARENT)
                setGravity(Gravity.BOTTOM)
                attributes.windowAnimations = R.style.DialogAnimationStyle
                attributes.y = 25
            }
        }
    }

    interface DialogOnClickListener {
        fun onContactInfoSelected(contactInfo: ContactInfo)
    }
}