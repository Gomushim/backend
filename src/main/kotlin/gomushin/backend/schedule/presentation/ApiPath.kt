package gomushin.backend.schedule.presentation

object ApiPath {
    const val SCHEDULES = "/v1/schedules"
    const val SCHEDULE = "/v1/schedules/{scheduleId}"
    const val SCHEDULES_BY_DATE = "/v1/schedules/date"
    const val SCHEDULES_BY_WEEK = "/v1/schedules/week"
    const val SCHEDULE_DETAIL = "/v1/schedules/detail/{scheduleId}"

    const val LETTERS = "/v1/schedules/letters"
    const val LETTER = "/v1/schedules/{scheduleId}/letters/{letterId}"
    const val LETTERS_BY_SCHEDULE = "/v1/schedules/{scheduleId}"
    const val LETTERS_MAIN = "/v1/schedules/letters/main"

    const val COMMENTS = "/v1/schedules/letters/{letterId}/comments"
    const val COMMENT = "/v1/schedules/letters/{letterId}/comments/{commentId}"
}
