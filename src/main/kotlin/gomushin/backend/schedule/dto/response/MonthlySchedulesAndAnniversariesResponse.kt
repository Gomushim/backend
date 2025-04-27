package gomushin.backend.schedule.dto.response

import gomushin.backend.couple.dto.response.MonthlyAnniversariesResponse

data class MonthlySchedulesAndAnniversariesResponse(
    val schedules: List<MonthlySchedulesResponse>,
//    val anniversaries: List<MonthlyAnniversariesResponse>,
) {
    companion object {
        fun of(
            schedules: List<MonthlySchedulesResponse>,
            anniversaries: List<MonthlyAnniversariesResponse>,
        ): MonthlySchedulesAndAnniversariesResponse {
            return MonthlySchedulesAndAnniversariesResponse(schedules)
        }
    }
}
