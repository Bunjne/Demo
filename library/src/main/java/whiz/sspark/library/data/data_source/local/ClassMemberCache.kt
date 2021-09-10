package whiz.sspark.library.data.data_source.local

import whiz.sspark.library.data.entity.Member
import java.util.*

interface ClassMemberCache {
    fun saveClassMembers(classId: String, member: Member)
    fun getClassMembers(classId: String): Member?
    fun getClassMembersLatestDateTime(): Date
}