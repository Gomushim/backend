package gomushin.backend.schedule.presentation

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.core.common.web.PageResponse
import gomushin.backend.core.common.web.response.ApiResponse
import gomushin.backend.schedule.dto.response.LetterDetailResponse
import gomushin.backend.schedule.dto.response.LetterPreviewResponse
import gomushin.backend.schedule.dto.response.MainLetterPreviewResponse
import gomushin.backend.schedule.facade.ReadLetterFacade
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "편지 조회", description = "ReadLetterController")
class ReadLetterController(
    private val readLetterFacade: ReadLetterFacade
) {

    @GetMapping(ApiPath.LETTERS_BY_SCHEDULE)
    @Operation(summary = "특정 일정의 편지 리스트 가져오기", description = "getLetterList")
    fun getLetterList(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @PathVariable scheduleId: Long,
    ): ApiResponse<List<LetterPreviewResponse>> {
        val letters = readLetterFacade.getList(customUserDetails, scheduleId)
        return ApiResponse.success(letters)
    }

    @GetMapping(ApiPath.LETTER)
    @Operation(summary = "특정 편지 가져오기", description = "getLetter")
    fun getLetter(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @PathVariable scheduleId: Long,
        @PathVariable letterId: Long,
    ): ApiResponse<LetterDetailResponse> {
        val letter = readLetterFacade.get(customUserDetails, scheduleId, letterId)
        return ApiResponse.success(letter)
    }

    @GetMapping(ApiPath.LETTERS)
    @Operation(summary = "커플 전체 편지 리스트 가져오기", description = "getLetterListToMe")
    fun getLetterListToMe(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
    ): PageResponse<LetterPreviewResponse> {
        val safePage = if (page < 1) 0 else page - 1
        val letters = readLetterFacade.getLetterListToCouple(customUserDetails, safePage, size)
        return letters
    }

    @GetMapping(ApiPath.LETTERS_MAIN)
    @Operation(summary = "메인화면 편지 리스트 가져오기", description = "getLetterListMain")
    fun getLetterListMain(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
    ): ApiResponse<List<MainLetterPreviewResponse>> {
        val letters = readLetterFacade.getLetterListMain(customUserDetails)
        return ApiResponse.success(letters)
    }

}
