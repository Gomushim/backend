package gomushin.backend.core.common.cache.configuration

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Configuration
import org.springframework.cache.CacheManager
import org.springframework.cache.caffeine.CaffeineCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import java.time.Duration

@Configuration
@EnableCaching
class LocalCacheConfiguration {

    @Primary
    @Bean
    fun localCacheManager(): CacheManager {
        val caffeineCacheManager = CaffeineCacheManager()
        caffeineCacheManager.setCaffeine(
            Caffeine.newBuilder()
                .maximumSize(100)
                .expireAfterWrite(Duration.ofMinutes(5))
        )
        return caffeineCacheManager
    }
}
