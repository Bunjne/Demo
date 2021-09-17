package whiz.sspark.library.data.data_source.local.impl

import st.lowlevel.storo.Storo
import whiz.sspark.library.data.data_source.local.ClassGroupCache
import whiz.sspark.library.data.entity.ClassGroup
import java.util.*
import java.util.concurrent.TimeUnit

class ClassGroupCacheImpl : ClassGroupCache {

    private val classGroupCacheKey = "ClassGroup"
    private val classGroupCacheTimeExpirySeconds = 600L
    private val classGroupDateTimeCacheKey = "ClassGroupDateTimeCacheKey"

    override fun saveClassGroups(classGroups: List<ClassGroup>) {
        Storo.put(classGroupCacheKey, classGroups)
            .setExpiry(classGroupCacheTimeExpirySeconds, TimeUnit.SECONDS)
            .execute()

        Storo.put(classGroupDateTimeCacheKey, Date())
            .execute()
    }

    override fun getClassGroups(): List<ClassGroup>? {
        val hasExpired = Storo.hasExpired(classGroupCacheKey).execute() ?: true
        return if (hasExpired) {
            null
        } else {
            Storo.get(classGroupCacheKey, Array<ClassGroup>::class.java).execute()?.toList()
        }
    }

    override fun getClassGroupLatestDateTime(): Date? {
        val hasExpired = Storo.hasExpired(classGroupDateTimeCacheKey).execute() ?: true
        return if (hasExpired) {
            null
        } else {
            Storo.get(classGroupDateTimeCacheKey, Date::class.java).execute() ?: null
        }
    }
}