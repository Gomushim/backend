package gomushin.backend.schedule.dto.response

data class DailySchedulesAndAnniversariesResponse(
    val schedules: List<DailyScheduleResponse>,
    val anniversaries: List<DailyAnniversaryResponse>,
) {
    companion object {
        fun of(
            schedules: List<DailyScheduleResponse>,
            anniversaries: List<DailyAnniversaryResponse>,
        ): DailySchedulesAndAnniversariesResponse {
            return DailySchedulesAndAnniversariesResponse(schedules, anniversaries)
        }
    }
}
