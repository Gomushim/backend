package gomushin.backend.core.common.web.response

import com.fasterxml.jackson.annotation.JsonAutoDetect
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import gomushin.backend.core.common.web.response.exception.ApiError
import io.swagger.v3.oas.annotations.media.Schema
import java.util.concurrent.CompletableFuture
import java.util.function.Function
import java.util.function.Supplier

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonAutoDetect(
    getterVisibility = JsonAutoDetect.Visibility.NONE,
    isGetterVisibility = JsonAutoDetect.Visibility.NONE,
    setterVisibility = JsonAutoDetect.Visibility.NONE
)
data class ApiResponse<T>(
    @get:JsonProperty("result")
    val result: T? = null,

    @get:JsonProperty("error")
    @Schema(hidden = true)
    val error: ApiError? = null,
) {
    init {
        require((result != null) xor (error != null)) {
            "ApiResponse must contain either result or error"
        }
    }

    val isSuccess: Boolean @JsonIgnore get() = error == null
    val hasError: Boolean @JsonIgnore get() = error != null

    fun orElse(other: T): T = result ?: other
    fun orElseGet(supplier: Supplier<out T>): T = result ?: supplier.get()

    fun <R> map(mapper: Function<in T, out R>): ApiResponse<R> =
        if (isSuccess) success(mapper.apply(result!!)) else error(error!!)

    fun <R> flatMap(mapper: Function<in T, ApiResponse<R>>): ApiResponse<R> =
        if (isSuccess) mapper.apply(result!!) else error(error!!)

    companion object {
        fun <R> success(result: R): ApiResponse<R> = ApiResponse(result = result)
        fun <R> error(error: ApiError): ApiResponse<R> = ApiResponse(error = error)
        fun <R> success(resultFuture: CompletableFuture<R>): CompletableFuture<ApiResponse<R>> =
            resultFuture.thenApply(Companion::success)
    }
}
