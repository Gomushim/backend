package gomushin.backend.core.common.cache.annotation.evict

import org.springframework.cache.annotation.CacheEvict

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
@CacheEvict("distributedCache", cacheManager = "distributedCacheManager", allEntries = true)
annotation class DistributedCacheEvict
