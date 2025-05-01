package gomushin.backend.schedule.dto.response

import gomushin.backend.schedule.domain.entity.Schedule
import java.time.LocalDateTime

data class ScheduleDetailResponse(
    val id : Long,
    val title : String,
    val fatigue : String,
    val startDate : LocalDateTime,
    val endDate : LocalDateTime,
    val letters : List<LetterPreviewResponse>
) {
    companion object {
        fun of(schedule : Schedule, letters: List<LetterPreviewResponse>) : ScheduleDetailResponse{
            return ScheduleDetailResponse(
                schedule.id,
                schedule.title,
                schedule.fatigue,
                schedule.startDate,
                schedule.endDate,
                letters
            )
        }
    }
}
