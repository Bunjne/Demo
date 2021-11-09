package whiz.sspark.library.data.repository

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import whiz.sspark.library.R
import whiz.sspark.library.data.data_source.remote.service.ManageAssignmentService
import whiz.sspark.library.data.entity.Attachment
import whiz.sspark.library.data.entity.DataWrapperX
import whiz.sspark.library.data.enum.AttachmentType
import whiz.sspark.library.data.static.DateTimePattern
import whiz.sspark.library.extension.convertToDateString
import whiz.sspark.library.extension.toJson
import whiz.sspark.library.utility.NetworkManager
import whiz.sspark.library.utility.fetchX
import java.util.*

interface ManageAssignmentRepository {
    suspend fun createAssignment(classGroupId: String, title: String, description: String, attachments: List<Attachment>, deadlineAt: Date): Flow<DataWrapperX<String>>
    suspend fun updateAssignment(classGroupId: String, assignmentId: String, title: String, description: String, attachments: List<Attachment>, deadlineAt: Date): Flow<DataWrapperX<String>>
}

open class ManageAssignmentRepositoryImpl(private val context: Context,
                                          private val remote: ManageAssignmentService): ManageAssignmentRepository {
    override suspend fun createAssignment(classGroupId: String, title: String, description: String, attachments: List<Attachment>, deadlineAt: Date): Flow<DataWrapperX<String>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val builder = MultipartBody.Builder()
                    with(builder) {
                        addFormDataPart("Title", title)
                        addFormDataPart("Message", description)
                    }

                    attachments.forEach {
                        if (it.type == AttachmentType.IMAGE.type) {
                            it.file?.let {
                                builder.addFormDataPart("Images", it.name, it.asRequestBody(MultipartBody.FORM))
                            }
                        } else {
                            it.file?.let {
                                builder.addFormDataPart("Files", it.name, it.asRequestBody(MultipartBody.FORM))
                            }
                        }
                    }

                    val serviceDateTime = deadlineAt.convertToDateString(DateTimePattern.serviceDateFullFormat)
                    builder.addFormDataPart("Deadline", serviceDateTime)

                    val response = remote.createAssignment(classGroupId, builder.build())
                    fetchX<String>(response)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun updateAssignment(classGroupId: String, assignmentId: String, title: String, description: String, attachments: List<Attachment>, deadlineAt: Date): Flow<DataWrapperX<String>> {
        return flow {
            if (NetworkManager.isOnline(context)) {
                try {
                    val builder = MultipartBody.Builder()
                    with(builder) {
                        addFormDataPart("Title", title)
                        addFormDataPart("Message", description)
                    }

                    attachments.forEach {
                        if (it.isLocal) {
                            if (it.type == AttachmentType.IMAGE.type) {
                                it.file?.let {
                                    builder.addFormDataPart("Images", it.name, it.asRequestBody(MultipartBody.FORM))
                                }
                            } else {
                                it.file?.let {
                                    builder.addFormDataPart("Files", it.name, it.asRequestBody(MultipartBody.FORM))
                                }
                            }
                        }
                    }

                    val serviceDateTime = deadlineAt.convertToDateString(DateTimePattern.serviceDateFullFormat)
                    val assignmentIds = attachments.filterNot { it.isLocal }.map { it.id }

                    builder.addFormDataPart("Deadline", serviceDateTime)

                    if (assignmentIds.isNotEmpty()) {
                        builder.addFormDataPart("AttachmentIds", assignmentIds.toJson())
                    }

                    val response = remote.updateAssignment(classGroupId, assignmentId, builder.build())
                    fetchX<String>(response)
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw Exception(context.resources.getString(R.string.general_alertmessage_no_internet_connection))
            }
        }.flowOn(Dispatchers.IO)
    }
}