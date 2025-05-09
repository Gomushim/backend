package gomushin.backend.core.common.cache.configuration

import org.springframework.cache.Cache
import java.util.concurrent.Callable

class MultiLayerCache(
    val localCache: Cache,
    val distributedCache: Cache,
) : Cache {
    override fun getName(): String {
        return localCache.name
    }

    override fun getNativeCache(): Any {
        return localCache.nativeCache
    }

    override fun get(key: Any): Cache.ValueWrapper? {
        var value = localCache.get(key)
        if (value == null) {
            value = distributedCache.get(key)
            if (value != null) {
                localCache.put(key, value.get())
            }
        }
        return value
    }

    override fun <T : Any?> get(key: Any, type: Class<T>?): T? {
        var value = localCache.get(key, type)
        if (value == null) {
            value = distributedCache.get(key, type)
            if (value != null) {
                localCache.put(key, value)
            }
        }
        return value
    }

    override fun <T : Any?> get(key: Any, valueLoader: Callable<T>): T? {
        var value = localCache.get(key, valueLoader)
        if (value == null) {
            value = distributedCache.get(key, valueLoader)
            if (value != null) {
                localCache.put(key, value)
            }
        }
        return value
    }

    override fun put(key: Any, value: Any?) {
        distributedCache.put(key, value)
        localCache.put(key, value)
    }

    override fun evict(key: Any) {
        localCache.evict(key)
        distributedCache.evict(key)
    }

    override fun clear() {
        localCache.clear()
        distributedCache.clear()
    }
}
