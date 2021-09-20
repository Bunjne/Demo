package whiz.sspark.library.data.entity

import whiz.sspark.library.data.enum.DataSource
import java.util.*

data class DataWrapperX<out T>(val data: T?,
                               val error: ApiResponseX?,
                               val statusCode: String? = null,
                               val isCacheExisted: Boolean = false,
                               val latestDateTime: Date? = null,
                               val isNetworkPreferred: Boolean? = true,
                               val dataSource: DataSource
)