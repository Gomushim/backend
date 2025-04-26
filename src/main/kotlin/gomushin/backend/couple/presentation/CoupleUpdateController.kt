package gomushin.backend.couple.presentation

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.core.common.web.response.ApiResponse
import gomushin.backend.couple.dto.request.UpdateMilitaryDateRequest
import gomushin.backend.couple.dto.request.UpdateRelationshipStartDateRequest
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
@Tag(name = "커플 정보 수정", description = "CoupleUpdateController")
class CoupleUpdateController(
    private val coupleFacade: CoupleFacade
) {
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(ApiPath.COUPLE_UPDATE_MILITARY_DATE)
    @Operation(summary = "입대, 전역일 수정 api", description = "입대일과 전역일을 수정함")
    fun updateMilitaryDate(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @RequestBody updateMilitaryDateRequest: UpdateMilitaryDateRequest
    ) : ApiResponse<Boolean> {
        coupleFacade.updateMilitaryDate(customUserDetails, updateMilitaryDateRequest)
        return ApiResponse.success(true)
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(ApiPath.COUPLE_UPDATE_RELATIONSHIP_DATE)
    @Operation(summary = "만난날 수정 api", description = "만난날을 수정함")
    fun updateRelationshipStartDate(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @RequestBody updateRelationshipStartDateRequest: UpdateRelationshipStartDateRequest
    ) : ApiResponse<Boolean> {
        coupleFacade.updateRelationshipStartDate(customUserDetails, updateRelationshipStartDateRequest)
        return ApiResponse.success(true)
    }
}