package gomushin.backend.couple.dto.response

import gomushin.backend.member.domain.entity.Member
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate

data class CoupleBirthDayResponse (
    @Schema(description = "커플 생년월일", example = "2001-04-01")
    val coupleBirthDay : LocalDate?,
    @Schema(description = "내 생년월일", example = "2001-03-01")
    val myBirthDay : LocalDate?
) {
    companion object {
        fun of(coupleMember : Member, myMember : Member) = CoupleBirthDayResponse(
            coupleMember.birthDate,
            myMember.birthDate
        )
    }
}