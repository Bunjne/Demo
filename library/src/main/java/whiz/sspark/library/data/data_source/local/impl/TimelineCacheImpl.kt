package whiz.sspark.library.data.data_source.local.impl

import st.lowlevel.storo.Storo
import whiz.sspark.library.data.data_source.local.TimelineCache
import whiz.sspark.library.data.entity.TimelineResponse
import java.util.concurrent.TimeUnit

class TimelineCacheImpl : TimelineCache {

    private val timelineKey = "Timeline"
    private val timelineExpiryDay = 1L

    private val storedKeyDates = mutableListOf<String>()

    override fun saveTimeline(timelineResponse: TimelineResponse, day: String) {
        val keyDate = "$timelineKey$day"
        storedKeyDates.add(keyDate)

        Storo.put(keyDate, timelineResponse)
            .setExpiry(timelineExpiryDay, TimeUnit.DAYS)
            .execute()
    }

    override fun getTimeline(day: String): TimelineResponse? {
        val cacheKey = "$timelineKey$day"
        if (!storedKeyDates.contains(cacheKey)) {
            storedKeyDates.add(cacheKey)
        }

        val hasExpired = Storo.hasExpired(cacheKey).execute() ?: true
        return if (hasExpired) {
            null
        } else {
            Storo.get("$timelineKey$day", TimelineResponse::class.java).execute() ?: null
        }
    }

    override fun clearTimeline() {
        storedKeyDates.forEach { keyDate ->
            Storo.delete(keyDate)
        }

        storedKeyDates.clear()
    }
}