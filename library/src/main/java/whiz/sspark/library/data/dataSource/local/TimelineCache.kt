package whiz.sspark.library.data.dataSource.local

import whiz.sspark.library.data.entity.TimelineResponse

interface TimelineCache {
    fun saveTimeline(timelineResponse: TimelineResponse, day: String)
    fun getTimeline(day: String): TimelineResponse?
    fun clearTimeline()
}