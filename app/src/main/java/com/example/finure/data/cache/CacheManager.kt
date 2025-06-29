package com.finure.app.data.cache

import java.util.concurrent.ConcurrentHashMap

data class CachedData<T>(
    val data: T,
    val timestamp: Long
)

object CacheManager {
    private val cacheMap = ConcurrentHashMap<String, CachedData<Any>>()

    fun <T> put(key: String, data: T, timestamp: Long = System.currentTimeMillis()) {
        cacheMap[key] = CachedData(data as Any, timestamp)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> get(key: String, expiryMillis: Long): T? {
        val cached = cacheMap[key] ?: return null
        return if (System.currentTimeMillis() - cached.timestamp < expiryMillis) {
            cached.data as T
        } else {
            cacheMap.remove(key)
            null
        }
    }

    fun clear(key: String) {
        cacheMap.remove(key)
    }

    fun clearAll() {
        cacheMap.clear()
    }
}