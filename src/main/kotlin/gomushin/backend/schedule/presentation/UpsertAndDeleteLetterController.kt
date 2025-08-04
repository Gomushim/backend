package gomushin.backend.schedule.presentation

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.core.common.web.response.ApiResponse
import gomushin.backend.core.infrastructure.filter.logging.LoggingFilter
import gomushin.backend.schedule.dto.request.UpsertLetterRequest
import gomushin.backend.schedule.facade.UpsertAndDeleteLetterFacade
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@Tag(name = "편지 생성 , 수정 , 삭제", description = "UpsertAndDeleteLetterController")
class UpsertAndDeleteLetterController(
    private val upsertAndDeleteLetterFacade: UpsertAndDeleteLetterFacade,
) {
    private val log = LoggerFactory.getLogger(UpsertAndDeleteLetterController::class.java)
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
        ApiPath.LETTERS
    )
    @Operation(summary = "편지 수정하거나 추가하기", description = "upsertLetter")
    fun upsertLetter(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @RequestPart upsertLetterRequest: UpsertLetterRequest,
        @RequestPart("pictures", required = false) pictures: List<MultipartFile>?,
    ): ApiResponse<Boolean> {
        log.info("[REQUEST LOG] userId={}, URL={}, Method={}, Body={}", customUserDetails.getId(), ApiPath.LETTERS, "POST", upsertLetterRequest)
        upsertAndDeleteLetterFacade.upsert(customUserDetails, upsertLetterRequest, pictures)
        return ApiResponse.success(true)
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(ApiPath.LETTER)
    @Operation(summary = "편지 삭제하기", description = "deleteLetter")
    fun deleteLetter(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @PathVariable scheduleId: Long,
        @PathVariable letterId: Long,
    ): ApiResponse<Boolean> {
        upsertAndDeleteLetterFacade.delete(customUserDetails, scheduleId, letterId)
        return ApiResponse.success(true)
    }
}
