package gomushin.backend.core.configuration.restclient

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatusCode
import org.springframework.web.client.RestClient

@Configuration
class RestClientConfiguration {

    @Bean
    fun restClient(): RestClient {
        return RestClient.builder()
            .defaultStatusHandler(HttpStatusCode::is4xxClientError) { request, response ->
                throw IllegalStateException("4xx 에러 발생 ${response.statusCode}")
            }
            .defaultStatusHandler(HttpStatusCode::is5xxServerError) { request, response ->
                throw IllegalStateException("5xx 에러 발생 ${response.statusCode}")
            }
            .build();
    }
}
