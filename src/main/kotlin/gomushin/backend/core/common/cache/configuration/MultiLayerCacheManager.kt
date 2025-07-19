package gomushin.backend.core.common.cache.configuration

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component
import java.util.stream.Collectors
import java.util.stream.Stream

@Component
class MultiLayerCacheManager(
    @Qualifier("localCacheManager") private val localCacheManager: CacheManager,
    @Qualifier("distributedCacheManager") private val distributedCacheManager: CacheManager
) : CacheManager {

    override fun getCache(name: String): Cache = MultiLayerCache(
        localCacheManager.getCache("localCache")!!,
        distributedCacheManager.getCache("distributedCache")!!
    )

    override fun getCacheNames(): MutableCollection<String> = Stream.concat(
        localCacheManager.cacheNames.stream(),
        distributedCacheManager.cacheNames.stream()
    ).collect(Collectors.toList())

}
