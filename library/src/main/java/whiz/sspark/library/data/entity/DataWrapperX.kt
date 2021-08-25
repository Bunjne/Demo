package whiz.sspark.library.data.entity

import whiz.sspark.library.data.enum.DATASOURCE
import java.util.*

data class DataWrapperX<out T>(val data: T?,
                               val error: ApiResponseX?,
                               val statusCode: String? = null,
                               val isCacheExisted: Boolean? = null,
                               val latestDateTime: Date? = null,
                               val isNetworkPreferred: Boolean? = true,
                               val dataSource: DATASOURCE
)