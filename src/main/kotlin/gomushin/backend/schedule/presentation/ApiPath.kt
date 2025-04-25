package gomushin.backend.schedule.presentation

object ApiPath {
    const val SCHEDULES = "/v1/schedules"
    const val SCHEDULE = "/v1/schedules/{scheduleId}"

    const val LETTERS = "/v1/schedules/letters"
    const val LETTER = "/v1/schedules/{scheduleId}/letters/{letterId}"

    const val COMMENTS = "/v1/schedules/letters/{letterId}/comments"
    const val COMMENT = "/v1/schedules/letters/{letterId}/comments/{commentId}"
}
