package whiz.sspark.library.di

import org.koin.dsl.module
import whiz.sspark.library.data.paging_source.AssignmentPagingSource

val pagingSourceModule = module {
    factory { (userId: String) -> AssignmentPagingSource(get(), userId) }
}
