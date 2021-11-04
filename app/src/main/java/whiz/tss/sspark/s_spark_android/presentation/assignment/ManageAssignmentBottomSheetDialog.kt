package whiz.tss.sspark.s_spark_android.presentation.assignment

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import com.github.dhaval2404.imagepicker.ImagePicker
import org.koin.androidx.viewmodel.ext.android.viewModel
import whiz.sspark.library.data.entity.Assignment
import whiz.sspark.library.data.entity.Attachment
import whiz.sspark.library.data.entity.CreateAssignment
import whiz.sspark.library.data.enum.AttachmentType
import whiz.sspark.library.data.viewModel.ManageAssignmentViewModel
import whiz.sspark.library.extension.toAccessiblePath
import whiz.sspark.library.extension.toCalendar
import whiz.sspark.library.extension.toJson
import whiz.sspark.library.extension.toObject
import whiz.sspark.library.utility.*
import whiz.tss.sspark.s_spark_android.databinding.FragmentManageAssignmentBinding
import whiz.tss.sspark.s_spark_android.presentation.BaseBottomSheetDialogFragment
import whiz.tss.sspark.s_spark_android.utility.showImage
import java.io.File
import java.util.*

class ManageAssignmentBottomSheetDialog: BaseBottomSheetDialogFragment() {

    companion object {
        private const val CREATE_ASSIGNMENT_DIRECTORY = "CreateAssignmentDialog"

        fun newInstance(classGroupId: String, oldAssignment: Assignment? = null) = ManageAssignmentBottomSheetDialog().apply {
            arguments = Bundle().apply {
                putString("classGroupId", classGroupId)

                if (oldAssignment != null) {
                    putString("oldAssignment", oldAssignment.toJson())
                }
            }
        }
    }

    private var filePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val fileUri = result.data?.data?.toAccessiblePath(requireContext(), folderPath)
            fileUri?.let {
                addAttachment(it, AttachmentType.FILE.type)
            }
        }
    }

    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val data = result.data
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri = data?.data?.path
            imageUri?.let {
                addAttachment(it, AttachmentType.IMAGE.type)
            }
        } else if (result.resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(requireContext(), ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        }
    }

    private val viewModel: ManageAssignmentViewModel by viewModel()

    private var _binding: FragmentManageAssignmentBinding? = null
    private val binding get() = _binding!!

    private val oldAssignment by lazy {
        arguments?.getString("oldAssignment")?.toObject<Assignment>()
    }
    
    private val isEditAssignment by lazy {
        oldAssignment != null
    }

    private val classGroupId by lazy {
        arguments?.getString("classGroupId") ?: ""
    }

    private val folderPath by lazy {
        File(requireContext().getExternalFilesDir(null), CREATE_ASSIGNMENT_DIRECTORY)
    }

    private lateinit var assignment: CreateAssignment

    private var listener: OnSaveSuccessfully? = null
    override val isForceFullScreen = true
    override val isDraggable = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentManageAssignmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listener = if (parentFragment != null) {
            parentFragment as OnSaveSuccessfully
        } else {
            activity as OnSaveSuccessfully
        }

        assignment = oldAssignment?.convertToCreateAssignment() ?: CreateAssignment()

        initView()

        folderPath.mkdirs()
    }

    override fun initView() {
        binding.vManageAssignment.init(
            isEditAssignment = isEditAssignment,
            onCancelClicked = {
                dismiss()
            },
            onCreateClicked = { title, description ->
                if (assignment.deadlineAt != null && title.isNotEmpty() && description.isNotEmpty()) {
                    if (isEditAssignment) {
                        viewModel.updateAssignment(
                            classGroupId = classGroupId,
                            assignmentId = oldAssignment!!.id,
                            title = title,
                            description = description,
                            attachments = assignment.attachments,
                            deadlineAt = assignment.deadlineAt!!
                        )

                    } else {
                        viewModel.createAssignment(
                            classGroupId = classGroupId,
                            title = title,
                            description = description,
                            attachments = assignment.attachments,
                            deadlineAt = assignment.deadlineAt!!
                        )
                    }
                } else {
                    requireContext().showAlertWithOkButton("Please fill all data")//TODO wait confirm message
                }
            },
            onSelectDateTimeClicked = {
                val today = Calendar.getInstance()
                val dateTime = assignment.deadlineAt?.toCalendar() ?: today
                showDatePicker(
                    context = requireContext(),
                    currentDate = dateTime,
                    minimumDate = today,
                    onDateSelected = { year, monthOfYear, dayOfMonth ->
                        showTimePicker(
                            context = requireContext(),
                            currentDate = dateTime,
                            onTimeSelected = { hourOfDay, minute ->
                                with(dateTime) {
                                    set(year, monthOfYear, dayOfMonth)
                                    set(Calendar.HOUR_OF_DAY, hourOfDay)
                                    set(Calendar.MINUTE, minute)
                                    set(Calendar.SECOND, 0)
                                    set(Calendar.MILLISECOND, 0)
                                }

                                assignment.deadlineAt = dateTime.time

                                binding.vManageAssignment.previewTime(assignment.deadlineAt!!)
                            }
                        )
                    }
                )
            },
            onAddImageClicked = {
                ImagePicker.with(this)
                    .crop()
                    .saveDir(folderPath)
                    .createIntent { intent ->
                        imagePickerLauncher.launch(intent)
                    }
            },
            onDeleteImageClicked = {
                assignment.attachments.removeAll { it.type == AttachmentType.IMAGE.type }
                binding.vManageAssignment.hidePreviewImage()
            },
            onImageClicked = { imageView, attachment ->
                if (attachment.isLocal) {
                    attachment.file?.let {
                        showImage(requireActivity(), imageView, it.path)
                    }
                } else {
                    showImage(requireActivity(), imageView, attachment.url)
                }
            },
            onAddFileClicked = {
                val intent = getFilePickerIntent()
                filePickerLauncher.launch(intent)
            },
            onDeleteFileClicked = {
                assignment.attachments.removeAll { it.type == AttachmentType.FILE.type }
                binding.vManageAssignment.hidePreviewFile()
            },
            onFileClicked = {
                val packageManager = requireContext().packageManager

                it.file?.let {
                    val fileUri = FileProvider.getUriForFile(
                        requireContext(),
                        requireContext().applicationContext.packageName + ".provider",
                        it
                    )

                    val mimeType = getMimeType(it)

                    val intent = Intent(Intent.ACTION_VIEW).apply {
                        setDataAndType(fileUri, mimeType)
                    }

                    val isAppSupported = intent.resolveActivityInfo(packageManager, 0) != null
                    if (isAppSupported) {
                        startActivity(Intent.createChooser(intent, null))
                    }
                } ?: run {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.url))
                        val isAppSupported = intent.resolveActivityInfo(packageManager, 0) != null
                        if (isAppSupported) {
                            startActivity(Intent.createChooser(intent, null))
                        }
                    } catch (e: NullPointerException) { }
                }
            }
        )

        oldAssignment?.let {
            binding.vManageAssignment.initAssignment(it)
        }
    }

    override fun observeView() {
        viewModel.viewLoading.observe(this) { isLoading ->
            if (isLoading) {
                loadingDialog.show()
            } else {
                loadingDialog.dismiss()
            }
        }
    }

    override fun observeData() {
        viewModel.manageAssignmentResponse.observe(this) {
            it?.let {
                requireContext().showAlertWithOkButton(it) {
                    listener?.onSaveSuccessfully()
                    dismiss()
                }
            }
        }
    }

    override fun observeError() {
        viewModel.manageAssignmentErrorResponse.observe(this) {
            it?.let {
                showApiResponseXAlert(requireContext(), it)
            }
        }

        viewModel.errorMessage.observe(this) {
            it?.let {
                requireContext().showAlertWithOkButton(it)
            }
        }
    }

    private fun addAttachment(path: String, type: String) {
        val file = File(path)

        val attachment = Attachment(
            name = file.name,
            file = file,
            type = type,
            isLocal = true,
            extension = if (type == AttachmentType.FILE.type) file.extension else ""
        )

        with(assignment.attachments) {
            removeAll { it.type == type }
            add(attachment)
        }

        if (type == AttachmentType.IMAGE.type) {
            binding.vManageAssignment.addImage(attachment)
        } else {
            binding.vManageAssignment.addFile(attachment)
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        deleteFileDirectory(folderPath)
        super.onDismiss(dialog)
    }

    interface OnSaveSuccessfully {
        fun onSaveSuccessfully()
    }
}