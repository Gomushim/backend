package gomushin.backend.schedule.dto.response

data class MainSchedulesAndAnniversariesResponse(
    val schedules: List<MainSchedulesResponse>,
    val anniversaries: List<MainAnniversariesResponse>,
) {
    companion object {
        fun of(
            schedules: List<MainSchedulesResponse>,
            anniversaries: List<MainAnniversariesResponse>,
        ): MainSchedulesAndAnniversariesResponse {
            return MainSchedulesAndAnniversariesResponse(schedules, anniversaries)
        }
    }
}

