package com.main.cache.impl;

import com.main.cache.Cache;
import com.main.cache.modal.Node;

import java.util.HashMap;
import java.util.Map;

/**
 * Least Recently used Cache
 * Removes the element from the cache that is least recently used in case cache is full
 * @param <K> class for key of the cache
 * @param <V> class for value of the cache
 */
public class CacheLRU<K, V> implements Cache<K, V> {

    private Integer capacity;
    private Node<K, V> head;
    private Node<K, V> tail;
    private Integer count;
    Map<K, Node<K, V>> cacheMap = new HashMap<>();

    public CacheLRU(Integer capacity){
        this.capacity = capacity;
        init();
    }

    /**
     * Initialize with default values
     */
    private void init(){
        this.count = 0;
        head = new Node<K, V>();
        tail = new Node<K, V>();
        head.setNext(tail);
        head.setPrev(null);
        tail.setNext(null);
        tail.setPrev(head);
    }

    public Integer getCount() {
        return count;
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

        if(cacheMap.containsKey(key)){
            //Key already exist in the cache, move the node to head
            Node<K, V> node = cacheMap.get(key);
            node.setValue(value);
            moveNodeToHead(node);
        } else {
            //new key received, inset the key to head
            if (count == capacity) {
                //Max capacity reached, free last node
                freeNodeFromTail();
            }
            Node<K, V> node = new Node<K, V>(key, value);
            cacheMap.put(key, node);
            insertNodeToHead(node);
            count++;
        }
        printCache();
    }

    /**
     * Move node to head
     * @param node
     */
    private void moveNodeToHead(Node<K, V> node) {
        freeNode(node);
        insertNodeToHead(node);
    }

    /**
     *
     * @param key
     * @return value of corresponding key or null if key is not present
     */
    public V get(K key) {
        if(!cacheMap.containsKey(key)){
            System.out.println("Returning NULL, Invalid key -> "+key);
            return null;
        }
        System.out.println("Fetching -> "+key);
        Node<K, V> node = cacheMap.get(key);
        moveNodeToHead(node);
        printCache();
        return node.getValue();
    }

    /**
     * print current status of the cache
     */
    public void printCache(){
        Node<K, V> aNode = head.getNext();
        if(aNode==tail){
            System.out.println("***********Empty Cache**********");
            return;
        }
        while(aNode != tail){
            System.out.print(aNode);
            aNode=aNode.getNext();
        }
        System.out.println("");
    }

    /**
     * Insert node to head
     * @param node
     */
    private void insertNodeToHead(Node<K, V> node){
        head.getNext().setPrev(node);
        node.setNext(head.getNext());
        node.setPrev(head);
        head.setNext(node);
    }

    /**
     * Free node connection
     * @param node
     */
    private void freeNode(Node<K, V> node){
        node.getPrev().setNext(node.getNext());
        node.getNext().setPrev(node.getPrev());
    }

    /**
     * Free node from tail
     * @return
     */
    public K freeNodeFromTail(){
        Node<K, V> node = tail.getPrev();
        System.out.println("Deleting last node (least recently used) "+node);
        cacheMap.remove(node.getKey());
        freeNode(node);
        count--;
        return node.getKey();
    }

    /**
     * Get last node key
     * @return
     */
    public K getLastNodeKey(){
        return tail.getPrev().getKey();
    }

    /**
     * Renove the key from the nodeList
     * @param key
     */
    public void remove(K key){
        Node<K, V> node = cacheMap.get(key);
        freeNode(node);
        cacheMap.remove(key);
        count--;
    }

    @Override
    public int size() {
        return count;
    }
}
