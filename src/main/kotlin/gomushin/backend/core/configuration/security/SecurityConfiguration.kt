package gomushin.backend.core.configuration.security

import gomushin.backend.core.oauth.handler.CustomSuccessHandler
import gomushin.backend.core.infrastructure.filter.JwtAuthenticationFilter
import gomushin.backend.core.jwt.JwtTokenProvider
import gomushin.backend.core.oauth.service.CustomOAuth2UserService
import gomushin.backend.core.service.CustomUserDetailsService
import gomushin.backend.member.domain.repository.MemberRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    private val jwtTokenProvider: JwtTokenProvider,
    private val memberRepository: MemberRepository
) {

    @Bean
    fun filterChain(
        http: HttpSecurity, corsConfiguration: CustomCorsConfiguration,
        customOAuth2UserService: CustomOAuth2UserService
    ): SecurityFilterChain {
        http
            .csrf {
                it.disable()
            }
            .cors {
                it.configurationSource(
                    corsConfiguration.corsConfigurationSource()
                )
            }
            .formLogin {
                it.disable()
            }
            .httpBasic {
                it.disable()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .oauth2Login { oAuth2LoginConfigurer ->
                oAuth2LoginConfigurer
                    .userInfoEndpoint { userInfoEndpointConfigurer ->
                        userInfoEndpointConfigurer
                            .userService(customOAuth2UserService)
                    }
                    .successHandler(CustomSuccessHandler(jwtTokenProvider, memberRepository))
            }
            .authorizeHttpRequests {
                it.requestMatchers(
                    "/",
                    "/v1/auth/**",
                    "/v1/oauth/**",
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/v3/api-docs/**",
                    "/swagger-resources/**",
                    "/webjars/**",
                    "/health",
                    "/swagger-ui/index.html",
                    "/favicon.ico",
                    "/error"
                ).permitAll()
            }
            .addFilterBefore(
                JwtAuthenticationFilter(
                    jwtTokenProvider,
                    CustomUserDetailsService(
                        memberRepository
                    )
                ),
                UsernamePasswordAuthenticationFilter::
                class.java
            )
        return http.build()
    }
}
