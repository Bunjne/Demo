package whiz.sspark.library.data.data_source.local

import whiz.sspark.library.data.entity.ClassGroup
import java.util.*

interface ClassGroupCache {
    fun saveClassGroups(classGroup: List<ClassGroup>)
    fun getClassGroups(): List<ClassGroup>?
    fun getClassGroupLatestDateTime(): Date?
}