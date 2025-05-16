package gomushin.backend.member.dto.request

import io.swagger.v3.oas.annotations.media.Schema

data class UpdateMyNickNameRequest (
    @Schema(description = "닉네임", example = "김꽃신")
    val nickname : String
)