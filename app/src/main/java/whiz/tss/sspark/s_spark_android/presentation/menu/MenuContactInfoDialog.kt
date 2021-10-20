package whiz.tss.sspark.s_spark_android.presentation.menu

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import whiz.sspark.library.data.entity.MenuContactInfoItem
import whiz.sspark.library.extension.setFullscreenWidth
import whiz.sspark.library.extension.toDP
import whiz.sspark.library.extension.toJson
import whiz.sspark.library.extension.toObject
import whiz.tss.sspark.s_spark_android.databinding.DialogMenuContactInfoSheetBinding
import whiz.tss.sspark.s_spark_android.R

class MenuContactInfoDialog : DialogFragment() {

    private var _binding: DialogMenuContactInfoSheetBinding? = null
    val binding get() = _binding!!

    companion object {
        fun newInstance(menuContactItem: MenuContactInfoItem) = MenuContactInfoDialog().apply {
            arguments = Bundle().apply {
                this.putString("menuContactItem", menuContactItem.toJson())
            }
        }
    }

    private val menuContactItem: MenuContactInfoItem? by lazy {
        arguments?.getString("menuContactItem")?.toObject<MenuContactInfoItem>()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreateDialog(savedInstanceState)
        _binding = DialogMenuContactInfoSheetBinding.inflate(LayoutInflater.from(context), null, false)
        val dialog = AlertDialog.Builder(requireContext()).create().apply {
            setView(binding.root)
            setCancelable(false)
            setDialogAnimation(window)
        }
        initViews()

        return dialog
    }

    override fun onStart() {
        super.onStart()
        setFullscreenWidth()
    }

    private fun initViews() {
        menuContactItem?.let {
            binding.vMenuContactInfo.init(
                contactInfo = it,
                onContactClicked = { description, contact ->
                    when (description) {
                        requireContext().resources.getString(R.string.menu_contact_info_personal_phone_text) -> {
                            callPhone(contact)
                        }
                        requireContext().resources.getString(R.string.menu_contact_info_office_phone_text) -> {
                            callPhone(contact)
                        }
                        requireContext().resources.getString(R.string.menu_contact_info_office_email_text) -> {
                            sendEmail(contact)
                        }
                        requireContext().resources.getString(R.string.menu_contact_info_personal_email_text) -> {
                            sendEmail(contact)
                        }
                    }
                },
                onCloseClicked = {
                    dialog?.dismiss()
                }
            )
        }
    }

    private fun sendEmail(emailContact: String) {
        val sendToOfficeEmailIntent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, emailContact)
        }
        startActivity(sendToOfficeEmailIntent)
    }

    private fun callPhone(phoneContact: String) {
        val callOfficePhoneIntent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:${phoneContact}")
        }
        startActivity(callOfficePhoneIntent)
    }

    private fun setDialogAnimation(window: Window?) {
        window?.let {
            with(window) {
                setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setGravity(Gravity.BOTTOM)
                attributes.y = 4.toDP(context)
                attributes.windowAnimations = R.style.VerticalSlidesAnimationStyle
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}