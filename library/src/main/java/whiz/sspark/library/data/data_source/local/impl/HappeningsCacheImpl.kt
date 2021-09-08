package whiz.sspark.library.data.data_source.local.impl

import st.lowlevel.storo.Storo
import whiz.sspark.library.data.data_source.local.HappeningsCache
import whiz.sspark.library.data.entity.Event
import java.util.*
import java.util.concurrent.TimeUnit

class HappeningsCacheImpl : HappeningsCache {

    private val eventListKey = "EventListKey"
    private val eventListLatestDateTimeKey = "EventListLatestDateTimeKey"
    private val eventListExpirySecond = 600L

    override fun saveEvents(events: List<Event>) {
        Storo.put(eventListKey, events)
            .setExpiry(eventListExpirySecond, TimeUnit.SECONDS)
            .execute()

        Storo.put(eventListLatestDateTimeKey, Date())
            .execute()
    }

    override fun listEvents(): List<Event>? {
        val hasExpired = Storo.hasExpired(eventListKey).execute() ?: true
        return if (hasExpired) {
            null
        } else {
            Storo.get(eventListKey, Array<Event>::class.java).execute()?.toList()
        }
    }

    override fun getEventListLatestDateTime(): Date {
        val hasExpired = Storo.hasExpired(eventListLatestDateTimeKey).execute() ?: true
        return if (hasExpired) {
            Date()
        } else {
            Storo.get(eventListLatestDateTimeKey, Date::class.java).execute() ?: Date()
        }
    }
}