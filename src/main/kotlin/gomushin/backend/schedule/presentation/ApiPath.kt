package gomushin.backend.schedule.presentation

object ApiPath {
    const val SCHEDULES = "/v1/schedules"
    const val SCHEDULE = "/v1/schedules/{scheduleId}"
    const val SCHEDULES_BY_DATE = "/v1/schedules/date"
    const val SCHEDULE_DETAIL = "/v1/schedules/detail/{scheduleId}"

    const val LETTERS = "/v1/schedules/letters"
    const val LETTER = "/v1/schedules/{scheduleId}/letters/{letterId}"
    const val LETTERS_TO_ME = "/v1/schedules/letters/to-me"
    const val LETTERS_BY_SCHEDULE = "/v1/schedules/{scheduleId}"

    const val COMMENTS = "/v1/schedules/letters/{letterId}/comments"
    const val COMMENT = "/v1/schedules/letters/{letterId}/comments/{commentId}"
}
