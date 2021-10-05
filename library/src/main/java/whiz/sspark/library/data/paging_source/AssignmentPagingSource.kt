package whiz.sspark.library.data.paging_source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bumptech.glide.load.HttpException
import whiz.sspark.library.data.data_source.remote.service.AssignmentService
import whiz.sspark.library.data.entity.AssignmentDTO
import whiz.sspark.library.data.entity.AssignmentItemDTO
import whiz.sspark.library.extension.toJson
import whiz.sspark.library.extension.toObject
import java.io.IOException

class AssignmentPagingSource(private val remote: AssignmentService,
                             private val termId: String): PagingSource<Int, AssignmentItemDTO>() {
    override fun getRefreshKey(state: PagingState<Int, AssignmentItemDTO>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, AssignmentItemDTO> {
        val pageIndex = params.key ?: 1
        return try {
            val response = remote.getAssignment(
                termId = termId,
                page = pageIndex,
                pageSize = 10
            )

            val data = response.body()?.data?.toJson()?.toObject<AssignmentDTO>()?.items?.toList() ?: listOf()

            val nextKey = if (data.isEmpty()) {
                null
            } else {
                pageIndex + 1
            }

            val previousKey = if (pageIndex == 1) {
                null
            } else {
                pageIndex - 1
            }

            LoadResult.Page(
                data = data,
                prevKey = previousKey,
                nextKey = nextKey
            )
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        }
    }
}