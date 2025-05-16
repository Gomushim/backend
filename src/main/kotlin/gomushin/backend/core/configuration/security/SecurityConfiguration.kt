package gomushin.backend.core.configuration.security

import gomushin.backend.core.configuration.redis.RedisService
import gomushin.backend.core.infrastructure.filter.CustomAuthenticationEntryPoint
import gomushin.backend.core.infrastructure.filter.JwtAuthenticationFilter
import gomushin.backend.core.jwt.infrastructure.TokenService
import gomushin.backend.core.oauth.handler.CustomAccessDeniedHandler
import gomushin.backend.core.oauth.handler.CustomSuccessHandler
import gomushin.backend.core.oauth.service.CustomOAuth2UserService
import gomushin.backend.core.service.CustomUserDetailsService
import gomushin.backend.couple.domain.repository.CoupleRepository
import gomushin.backend.member.domain.repository.MemberRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsUtils

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    private val tokenService: TokenService,
    private val memberRepository: MemberRepository,
    private val redisService: RedisService,
    @Value("\${redirect-url}") private val redirectUrl: String,
    @Value("\${cookie.domain}") private val cookieDomain: String
) {

    @Bean
    fun filterChain(
        http: HttpSecurity, corsConfiguration: CustomCorsConfiguration,
        customOAuth2UserService: CustomOAuth2UserService, coupleRepository: CoupleRepository,
        customAccessDeniedHandler: CustomAccessDeniedHandler,
        customAuthenticationEntryPoint: CustomAuthenticationEntryPoint,
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
                    .successHandler(
                        CustomSuccessHandler(
                            tokenService,
                            memberRepository,
                            redisService,
                            cookieDomain,
                            redirectUrl,
                        )
                    )
            }
            .exceptionHandling {
                it.accessDeniedHandler(customAccessDeniedHandler)
                it.authenticationEntryPoint(customAuthenticationEntryPoint)
            }
            .authorizeHttpRequests {
                it.requestMatchers(
                    "/",
                    "/v1/auth/**",
                    "/v1/oauth/**",
                    "/oauth2/**",
                    "/oauth2/authorization/**",
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
                it.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                it.requestMatchers("/v1/member/onboarding").hasRole("GUEST")
                it.anyRequest().hasRole("MEMBER")
            }
            .addFilterBefore(
                JwtAuthenticationFilter(
                    tokenService,
                    CustomUserDetailsService(
                        memberRepository,
                        coupleRepository,
                    )
                ),
                UsernamePasswordAuthenticationFilter::
                class.java
            )
        return http.build()
    }
}
