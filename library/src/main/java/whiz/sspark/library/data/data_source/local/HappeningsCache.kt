package whiz.sspark.library.data.data_source.local

import whiz.sspark.library.data.entity.Event
import java.util.*

interface HappeningsCache {
    fun saveEvents(events: List<Event>)
    fun listEvents(): List<Event>?
    fun getEventListLatestDateTime(): Date
}