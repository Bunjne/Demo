package whiz.sspark.library.data.data_source.local.impl

import st.lowlevel.storo.Storo
import whiz.sspark.library.data.data_source.local.ClassMemberCache
import whiz.sspark.library.data.entity.Member
import java.util.*
import java.util.concurrent.TimeUnit

class ClassMemberCacheImpl : ClassMemberCache {

    private val classMemberCacheKey = "classMemberCacheKey"
    private val classMemberDateTimeCacheKey = "classMemberDateTimeCacheKey"
    private val classMemberCacheExpiryDays = 5L

    override fun saveClassMembers(classId: String, member: Member) {
        val classMemberKey = "$classMemberCacheKey$classId"

        Storo.put(classMemberKey, member)
            .setExpiry(classMemberCacheExpiryDays, TimeUnit.DAYS)
            .execute()

        Storo.put(classMemberDateTimeCacheKey, Date())
            .execute()
    }

    override fun getClassMembers(classId: String): Member? {
        val classMemberKey = "$classMemberCacheKey$classId"

        val hasExpired = Storo.hasExpired(classMemberKey).execute() ?: true
        return if (hasExpired) {
            null
        } else {
            Storo.get(classMemberKey, Member::class.java)?.execute()
        }
    }

    override fun getClassMembersLatestDateTime(): Date {
        val hasExpired = Storo.hasExpired(classMemberDateTimeCacheKey).execute() ?: true
        return if (hasExpired) {
            Date()
        } else {
            Storo.get(classMemberDateTimeCacheKey, Date::class.java).execute() ?: Date()
        }
    }
}