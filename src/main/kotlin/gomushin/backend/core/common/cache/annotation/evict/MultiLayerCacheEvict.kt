package gomushin.backend.core.common.cache.annotation.evict

import org.springframework.cache.annotation.CacheEvict

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@CacheEvict("multiLayerCache", cacheManager = "multiLayerCacheManager", allEntries = true)
annotation class MultiLayerCacheEvict
