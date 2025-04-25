package gomushin.backend.schedule.presentation

import gomushin.backend.core.CustomUserDetails
import gomushin.backend.core.common.web.response.ApiResponse
import gomushin.backend.schedule.dto.UpsertCommentRequest
import gomushin.backend.schedule.facade.UpsertAndDeleteCommentFacade
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "댓글 생성 , 수정 , 삭제", description = "UpsertAndDeleteCommentController")
class UpsertAndDeleteCommentController(
    private val upsertAndDeleteCommentFacade: UpsertAndDeleteCommentFacade,
) {

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(ApiPath.COMMENTS)
    @Operation(summary = "댓글 수정하거나 추가하기", description = "upsertComment")
    fun upsertComment(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @PathVariable letterId: Long,
        @RequestBody upsertCommentRequest: UpsertCommentRequest,
    ): ApiResponse<Boolean> {
        upsertAndDeleteCommentFacade.upsert(customUserDetails, letterId, upsertCommentRequest)
        return ApiResponse.success(true)
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(ApiPath.COMMENT)
    @Operation(summary = "댓글 삭제하기", description = "deleteComment")
    fun deleteComment(
        @AuthenticationPrincipal customUserDetails: CustomUserDetails,
        @PathVariable letterId: Long,
        @PathVariable commentId: Long,
    ): ApiResponse<Boolean> {
        upsertAndDeleteCommentFacade.delete(customUserDetails, letterId, commentId)
        return ApiResponse.success(true)
    }
}
