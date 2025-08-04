package gomushin.backend.couple.presentation

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.core.common.web.PageResponse
import gomushin.backend.core.common.web.response.ApiResponse
import gomushin.backend.couple.dto.request.GenerateAnniversaryRequest
import gomushin.backend.couple.dto.response.AnniversaryDetailResponse
import gomushin.backend.couple.dto.request.UpdateAnniversaryRequest
import gomushin.backend.couple.dto.response.MainAnniversaryResponse
import gomushin.backend.couple.dto.response.TotalAnniversaryResponse
import gomushin.backend.couple.facade.AnniversaryFacade
import gomushin.backend.couple.facade.CoupleFacade
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@Tag(name = "기념일", description = "AnniversaryController")
class AnniversaryController(
    private val coupleFacade: CoupleFacade,
    private val anniversaryFacade: AnniversaryFacade
) {
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(ApiPath.ANNIVERSARY_GENERATE)
    @Operation(
        summary = "기념일 생성",
        description = "generateAnniversary"
    )
    fun generateAnniversary(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @RequestBody generateAnniversaryRequest: GenerateAnniversaryRequest
    ): ApiResponse<Boolean> {
        coupleFacade.generateAnniversary(customUserDetails, generateAnniversaryRequest)
        return ApiResponse.success(true)
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(ApiPath.ANNIVERSARY)
    @Operation(
        summary = "기념일 수정",
        description = "updateAnniversary"
    )
    fun updateAnniversary(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @PathVariable anniversaryId: Long,
        @RequestBody updateAnniversaryRequest: UpdateAnniversaryRequest,
    ): ApiResponse<Boolean> {
        anniversaryFacade.updateAnniversary(customUserDetails, anniversaryId, updateAnniversaryRequest)
        return ApiResponse.success(true)
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(ApiPath.ANNIVERSARY)
    @Operation(
        summary = "기념일 삭제",
        description = "deleteAnniversary"
    )
    fun deleteAnniversary(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @PathVariable anniversaryId: Long
    ): ApiResponse<Boolean> {
        anniversaryFacade.delete(customUserDetails, anniversaryId)
        return ApiResponse.success(true)
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(ApiPath.ANNIVERSARY_MAIN)
    @Operation(
        summary = "메인 - 가까운 3개의 기념일 조회",
        description = "getAnniversariesMain"
    )
    fun getAnniversariesMain(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails
    ): ApiResponse<List<MainAnniversaryResponse>> =
        ApiResponse.success(anniversaryFacade.getAnniversaryListMain(customUserDetails))

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(ApiPath.ANNIVERSARIES)
    @Operation(
        summary = "기념일 리스트 조회",
        description = "getAnniversaries"
    )
    fun getAnniversaryList(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int,
    ): PageResponse<TotalAnniversaryResponse> {
        val safePage = if (page < 1) 0 else page - 1
        val anniversaries = anniversaryFacade.getAnniversaryList(customUserDetails, safePage, size)
        return anniversaries
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(ApiPath.ANNIVERSARY)
    @Operation(
        summary = "기념일 상세 조회",
        description = "getAnniversary"
    )
    fun getAnniversary(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @PathVariable anniversaryId: Long
    ): ApiResponse<AnniversaryDetailResponse> {
        val anniversary = anniversaryFacade.get(customUserDetails, anniversaryId)
        return ApiResponse.success(anniversary)
    }


}
