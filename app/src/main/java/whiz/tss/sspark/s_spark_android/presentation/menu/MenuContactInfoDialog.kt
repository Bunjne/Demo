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

    private val callPersonalPhoneIntent: Intent by lazy {
        Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:${menuContactItem?.personalPhone}")
        }
    }

    private val callOfficePhoneIntent: Intent by lazy {
        Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:${menuContactItem?.officePhone}")
        }
    }

    private val sendToOfficeEmailIntent: Intent by lazy {
        Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, menuContactItem?.officeEmail)
        }
    }

    private val sendToPersonalEmailIntent: Intent by lazy {
        Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL, menuContactItem?.personalEmail)
        }
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
                onContactClicked = { description ->
                    when (description) {
                        requireContext().resources.getString(R.string.menu_contact_info_personal_phone_text) -> {
                            startActivity(callPersonalPhoneIntent)
                        }
                        requireContext().resources.getString(R.string.menu_contact_info_office_phone_text) -> {
                            startActivity(callOfficePhoneIntent)
                        }
                        requireContext().resources.getString(R.string.menu_contact_info_office_email_text) -> {
                            startActivity(sendToOfficeEmailIntent)
                        }
                        requireContext().resources.getString(R.string.menu_contact_info_personal_email_text) -> {
                            startActivity(sendToPersonalEmailIntent)
                        }
                    }
                },
                onCloseClicked = {
                    dialog?.dismiss()
                }
            )
        }
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