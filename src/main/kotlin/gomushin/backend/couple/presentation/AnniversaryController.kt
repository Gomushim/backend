package gomushin.backend.couple.presentation

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.core.common.web.response.ApiResponse
import gomushin.backend.couple.dto.request.GenerateAnniversaryRequest
import gomushin.backend.couple.facade.CoupleFacade
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "기념일 생성", description = "AnniversaryController")
class AnniversaryController(
    private val coupleFacade: CoupleFacade
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
}