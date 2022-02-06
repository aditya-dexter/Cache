package com.main.cache;

import com.main.cache.impl.CacheLFU;
import com.main.cache.impl.CacheLRU;
import com.main.cache.impl.CacheMFU;
import com.main.cache.impl.CacheMRU;

/**
 * A cache template that supports cache for various eviction policies
 * @param <K> class for key of the cache
 * @param <V> class for value of the cache
 */
public interface Cache<K, V> {
    /**
     * A method to put key and corresponding value in the cache
     * If key already exists, the value will be replaced
     * A put operation on existing key is also a read operation
     * @param key
     * @param value cannot be null
     * @throws IllegalArgumentException if the value is null
     */
    public void put(K key, V value) throws IllegalArgumentException;

    /**
     * A method to fetch the value of corresponding key from the cache
     * @param key
     * @return value of the corresponding key or null if key is not present
     */
    public V get(K key);

    /***
     * Method that returns size of the array
     * @return current size of the cache
     */
    public int size();

    /**
     *
     * @param evictionPolicy the eviction policy to be used in case the cache is full
     * @param capacity the capacity of the cache
     * @param <T> class for key
     * @param <U> class for value
     * @return Object of the cache with given capacity and that supports given eviction policy
     * @throws IllegalArgumentException in case the evictionPolicy is not valid check {@code com.main.cache.EvictionPolicy}
     */
    public static <T, U> Cache create(EvictionPolicy evictionPolicy, int capacity){
        switch (evictionPolicy) {
            case LRU:
                return new CacheLRU<T, U>(capacity);
            case MRU:
                return new CacheMRU<T, U>(capacity);
            case LFU:
                return new CacheLFU<T, U>(capacity);
            case MFU:
                return new CacheMFU<T, U>(capacity);
            default:
                throw new IllegalArgumentException("Not a valid evictionPolicy");
        }
    }
}
