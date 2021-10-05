package whiz.sspark.library.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import whiz.sspark.library.data.data_source.remote.service.AssignmentService
import whiz.sspark.library.data.entity.AssignmentItemDTO
import whiz.sspark.library.data.paging_source.AssignmentPagingSource

interface AssignmentRepository {
    suspend fun getAssignment(termId: String): Flow<PagingData<AssignmentItemDTO>>
}

class AssignmentRepositoryImpl(private val remote: AssignmentService): AssignmentRepository {
    override suspend fun getAssignment(termId: String): Flow<PagingData<AssignmentItemDTO>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                AssignmentPagingSource(remote, termId)
            }
        ).flow
    }
}