package com.example.finure.data.cache

import java.util.concurrent.ConcurrentHashMap

/**
 * Data wrapper that holds a cached object and its timestamp.
 *
 * @param T Type of the cached object.
 * @property data The actual cached data.
 * @property timestamp The time (in millis) when the data was cached.
 */
data class CachedData<T>(
    val data: T,
    val timestamp: Long
)

/**
 * In-memory cache manager for storing and retrieving API responses or computed data.
 *
 * This object provides basic caching capabilities with optional expiry support.
 * It is thread-safe and can be used globally across the app.
 */
object CacheManager {

    // ConcurrentHashMap ensures thread-safe read/write access
    private val cacheMap = ConcurrentHashMap<String, CachedData<Any>>()

    /**
     * Stores data in cache against a given key.
     *
     * @param key Unique identifier for the cached data.
     * @param data The actual object to cache.
     * @param timestamp Optional timestamp (defaults to current time).
     */
    fun <T> put(key: String, data: T, timestamp: Long = System.currentTimeMillis()) {
        cacheMap[key] = CachedData(data as Any, timestamp)
    }

    /**
     * Retrieves data from cache if not expired.
     *
     * @param key The cache key.
     * @param expiryMillis Time in milliseconds after which the cache is considered expired.
     * @return Cached data if present and valid, or null otherwise.
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> get(key: String, expiryMillis: Long): T? {
        val cached = cacheMap[key] ?: return null
        return if (System.currentTimeMillis() - cached.timestamp < expiryMillis) {
            cached.data as T
        } else {
            cacheMap.remove(key) // Remove expired entry
            null
        }
    }

    /**
     * Removes a specific entry from the cache.
     *
     * @param key The key to be cleared.
     */
    fun clear(key: String) {
        cacheMap.remove(key)
    }

    /**
     * Clears all cached data.
     */
    fun clearAll() {
        cacheMap.clear()
    }
}
