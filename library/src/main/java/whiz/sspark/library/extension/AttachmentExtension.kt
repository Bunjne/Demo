package whiz.sspark.library.extension

import whiz.sspark.library.data.entity.Attachment
import whiz.sspark.library.data.enum.AttachmentType

fun List<Attachment>.toAttachmentImages() = this.filter { it.type == AttachmentType.IMAGE.type }

fun List<Attachment>.toAttachmentFiles() = this.filter { it.type == AttachmentType.FILE.type }