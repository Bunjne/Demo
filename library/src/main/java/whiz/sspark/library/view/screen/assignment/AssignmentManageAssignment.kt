package whiz.sspark.library.view.screen.assignment

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import whiz.sspark.library.R
import whiz.sspark.library.data.entity.Assignment
import whiz.sspark.library.data.entity.Attachment
import whiz.sspark.library.data.enum.AttachmentType
import whiz.sspark.library.data.static.DateTimePattern
import whiz.sspark.library.databinding.ViewManageAssignmentFragmentBinding
import whiz.sspark.library.extension.convertToDateString
import whiz.sspark.library.extension.show
import java.util.*

class AssignmentManageAssignment: ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val binding by lazy {
        ViewManageAssignmentFragmentBinding.inflate(LayoutInflater.from(context), this, true)
    }

    private var onImageClicked: (ImageView, Attachment) -> Unit = { _, _ ->  }
    private var onFileClicked: (Attachment) -> Unit = { }

    fun init(isEditAssignment: Boolean,
             onCancelClicked: () -> Unit,
             onCreateClicked: (String, String) -> Unit,
             onSelectDateTimeClicked: () -> Unit,
             onAddImageClicked: () -> Unit,
             onDeleteImageClicked: () -> Unit,
             onImageClicked: (ImageView, Attachment) -> Unit,
             onAddFileClicked: () -> Unit,
             onDeleteFileClicked: () -> Unit,
             onFileClicked: (Attachment) -> Unit) {

        this.onImageClicked = onImageClicked
        this.onFileClicked = onFileClicked

        if (isEditAssignment) {
            binding.tvTitle.text = resources.getString(R.string.assignment_edit_assignment)
            binding.tvCreate.text = resources.getString(R.string.general_save_text)
        } else {
            binding.tvTitle.text = resources.getString(R.string.assignment_create_assignment)
            binding.tvCreate.text = resources.getString(R.string.general_create_text)
        }

        binding.tvCancel.setOnClickListener {
            onCancelClicked()
        }

        binding.tvCreate.setOnClickListener {
            val title = binding.etTitle.text?.toString() ?: ""
            val description = binding.etDescription.text?.toString() ?: ""
            onCreateClicked(title, description)
        }

        binding.tvAssignmentDeadline.setOnClickListener {
            onSelectDateTimeClicked()
        }

        binding.vAddImage.setOnClickListener {
            onAddImageClicked()
        }

        binding.vAddFile.setOnClickListener {
            onAddFileClicked()
        }

        with(binding.ivDeleteImage) {
            show(R.drawable.ic_bin)
            setOnClickListener {
                onDeleteImageClicked()
            }
        }

        with(binding.ivDeleteFile) {
            show(R.drawable.ic_bin)
            setOnClickListener {
                onDeleteFileClicked()
            }
        }

        binding.vAttachmentFile.setContainerBackground(R.drawable.bg_editable_text)
    }

    fun initAssignment(assignment: Assignment) {
        with(assignment) {
            binding.etTitle.setText(title)
            binding.etDescription.setText(description)
            previewTime(deadlineAt)
        }

        assignment.attachments.forEach {
            if (it.type == AttachmentType.IMAGE.type) {
                binding.vAttachmentImage.init(it, onImageClicked)
                binding.gImage.visibility = View.VISIBLE
            } else {
                binding.vAttachmentFile.init(it, onFileClicked)
                binding.gFile.visibility = View.VISIBLE
            }
        }
    }

    fun previewTime(deadlineAt: Date) {
        val date = deadlineAt.convertToDateString(
            defaultPattern = DateTimePattern.todayAbbreviatedDateFormatEn,
            dayMonthThPattern = DateTimePattern.dayFullMonthFormatTh,
            yearThPattern = DateTimePattern.generalYear
        )

        val time = deadlineAt.convertToDateString(
            defaultPattern = DateTimePattern.generalShortTime
        )

        binding.tvAssignmentDeadline.text = resources.getString(R.string.class_attendance_class_check_date_place_holder, date, time)

    }

    fun hidePreviewImage() {
        binding.vAttachmentImage.clearImage()
        binding.gImage.visibility = View.GONE
    }

    fun hidePreviewFile() {
        binding.gFile.visibility = View.GONE
    }

    fun addFile(attachment: Attachment) {
        binding.vAttachmentFile.init(attachment, onFileClicked)
        binding.gFile.visibility = View.VISIBLE
    }

    fun addImage(attachment: Attachment) {
        binding.vAttachmentImage.init(attachment, onImageClicked)
        binding.gImage.visibility = View.VISIBLE
    }
}