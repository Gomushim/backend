package gomushin.backend.schedule.presentation

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.core.common.web.response.ApiResponse
import gomushin.backend.schedule.dto.UpsertLetterRequest
import gomushin.backend.schedule.facade.UpsertAndDeleteLetterFacade
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@Tag(name = "편지 생성 , 수정 , 삭제", description = "UpsertAndDeleteLetterController")
class UpsertAndDeleteLetterController(
    private val upsertAndDeleteLetterFacade: UpsertAndDeleteLetterFacade,
) {
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(
        ApiPath.LETTERS,
        consumes = [MediaType.MULTIPART_FORM_DATA_VALUE]
    )
    @Operation(summary = "편지 수정하거나 추가하기", description = "upsertLetter")
    fun upsertLetter(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @RequestPart("data") upsertLetterRequest: UpsertLetterRequest,
        @RequestPart("pictures", required = false) pictures: List<MultipartFile>?,
    ): ApiResponse<Boolean> {
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
