package com.main.cache.impl;

import com.main.cache.Cache;

import java.util.HashMap;
import java.util.Map;

/**
 * Most Frequently used Cache
 * Removes the element from the cache that is Most Frequently used in case cache is full
 * In case two or more element has same (max) frequency, the least recently used element would be removed
 * @param <K> class for key of the cache
 * @param <V> class for value of the cache
 */
public class CacheMFU<K, V> implements Cache<K, V> {

    private Integer capacity;
    Map<K, Integer> keyFrequencyMap = new HashMap<>();
    Map<Integer, CacheLRU<K, V>> frequencyNodeListMap = new HashMap<>();
    private int thisCount;
    private int maxFrequency = 1;
    private int secondMaxFreq = 1;

    public CacheMFU(Integer capacity){
        this.capacity = capacity;
    }

    /**
     *
     * @param key
     * @param value cannot be null
     * @throws IllegalArgumentException if value is null
     */
    @Override
    public void put(K key, V value){
        if(value == null) {
            //Value cannot be null, throw exception
            throw new IllegalArgumentException("Null value not allowed");
        }

        if(keyFrequencyMap.containsKey(key)){
            //Key already exist in the cache, increase the frequency of the key
            increaseFrequency(key, value);
        } else {
            //new key received, inset the key with frequency 1
            if (thisCount == capacity) {
                //Max capacity reached, free most frequently used node
                freeMaxFreqNode();
            }
            insertNewKey(key, value);
            thisCount++;
        }
        printCache();
    }

    /**
     * Free node with max frequency
     */
    private void freeMaxFreqNode() {
        K key = frequencyNodeListMap.get(maxFrequency).getLastNodeKey();
        removeElement(key);
        keyFrequencyMap.remove(key);
        thisCount--;
    }

    /**
     * Insert new key
     * @param key
     * @param value
     */
    private void insertNewKey(K key, V value) {
        keyFrequencyMap.put(key, 1);
        CacheLRU cacheLRU = frequencyNodeListMap.get(1);
        if(cacheLRU != null){
            cacheLRU.put(key, value);
        } else {
            cacheLRU = new CacheLRU(capacity);
            cacheLRU.put(key, value);
            frequencyNodeListMap.put(1, cacheLRU);
        }
    }

    /**
     * increase frequency of the key as the key is accessed
     * @param key
     * @param value
     */
    private void increaseFrequency(K key, V value) {
        Integer currentFrequency = keyFrequencyMap.get(key);
        removeElement(key);

        CacheLRU cacheLRU = frequencyNodeListMap.get(currentFrequency+1);
        if(cacheLRU != null){
            cacheLRU.put(key, value);
        } else {
            cacheLRU = new CacheLRU(capacity);
            cacheLRU.put(key, value);
            frequencyNodeListMap.put(currentFrequency+1, cacheLRU);
        }
        keyFrequencyMap.put(key, currentFrequency+1);
        if(maxFrequency <= currentFrequency+1){
            maxFrequency = currentFrequency+1;
        } else if(secondMaxFreq < currentFrequency+1){
            secondMaxFreq = currentFrequency+1;
        }
    }

    /**
     * remove the key from the cache
     * @param key
     */
    private void removeElement(K key) {
        Integer freq = keyFrequencyMap.get(key);
        CacheLRU cacheLRU = frequencyNodeListMap.get(freq);
        cacheLRU.remove(key);
        if(cacheLRU.getCount()==0){
            //nodeList is empty, remove the frequency from the frequencyNodeListMap
            if(maxFrequency == freq){
                maxFrequency = secondMaxFreq;
            }
            frequencyNodeListMap.remove(freq);
        }
    }

    /**
     *
     * @param key
     * @return value of corresponding key or null if key is not present
     */
    @Override
    public V get(K key){
        if(!keyFrequencyMap.containsKey(key)){
            System.out.println("Invalid key -> "+key);
            return null;
        }
        V value = frequencyNodeListMap.get(keyFrequencyMap.get(key)).get(key);
        increaseFrequency(key, value);
        printCache();
        return value;
    }

    /**
     * print current status of the cache
     */
    private void printCache(){
        System.out.println("CacheMap= " + keyFrequencyMap + ", maxFrequency = "
                + maxFrequency + ", secondMaxFreq = " + secondMaxFreq);
        for(Integer key : frequencyNodeListMap.keySet()) {
            System.out.print("Frequency[" + key+"] ");
            frequencyNodeListMap.get(key).printCache();
        }
    }

    @Override
    public int size() {
        return thisCount;
    }
}
