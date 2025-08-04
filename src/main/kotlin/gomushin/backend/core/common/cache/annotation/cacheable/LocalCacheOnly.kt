package gomushin.backend.core.common.cache.annotation.cacheable

import org.springframework.cache.annotation.Cacheable

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@Cacheable("localCache", cacheManager = "localCacheManager")
annotation class LocalCacheOnly
