package com.main.cache.impl;

import com.main.cache.Cache;
import com.main.cache.modal.Node;

import java.util.HashMap;
import java.util.Map;

/**
 * Most Recently used Cache
 * Removes the element from the cache that is most recently used in case cache is full
 * Internally uses doubly linkedList to maintain the nodes
 * The most recently used node is moved to tail and then removed if the max capacity is reached
 * @param <K> class for key of the cache
 * @param <V> class for value of the cache
 */
public class CacheMRU<K, V> implements Cache<K, V> {

    private Integer capacity;
    private Node<K, V> head;
    private Node<K, V> tail;
    Map<K, Node<K, V>> cacheMap = new HashMap<>();
    private int count;

    public CacheMRU(Integer capacity){
        this.capacity = capacity;
        init();
    }

    /**
     * initialize default values
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


    /**
     * put key and corresponding value in the cache
     * @param key
     * @param value cannot be null
     * @throws IllegalArgumentException if the value is null
     */
    @Override
    public void put(K key, V value){
        if(value == null) {
            //throw exception as the null value is not allowed
            throw new IllegalArgumentException("Null value not allowed");
        }

        if(cacheMap.containsKey(key)){
            //key already exist, replace the value and move the node to tail
            Node<K, V> node = cacheMap.get(key);
            node.setValue(value);
            moveNodeToTail(node);
        } else {
            // new key received, inset the new nod in the cache
            if (count == capacity) {
                //maximum capacity reached, remove an element from the tail
                freeNodeFromTail();
                count--;
            }
            Node<K, V> node = new Node<K, V>(key, value);
            cacheMap.put(key, node);
            //insert new node to the tail
            insertNodeAtTail(node);
            count++;
        }
        printCache();
    }

    /**
     *
     * @param node to be moved to tail
     */
    private void moveNodeToTail(Node<K, V> node) {
        freeNode(node);
        insertNodeAtTail(node);
    }

    /**
     *
     * @param key
     * @return value of the corresponding key or null if key does not exist
     */
    public V get(K key){
        if(!cacheMap.containsKey(key)){
            System.out.println("Invalid key -> "+key);
            return null;
        }
        Node<K, V> node = cacheMap.get(key);
        //since the key is accessed, move the node to tail
        moveNodeToTail(node);
        printCache();
        return node.getValue();

    }

    /**
     * print the cache status
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
     *
     * @param node to be inserted at tail
     */
    private void insertNodeAtTail(Node<K, V> node){
        node.setPrev(tail.getPrev());
        node.setNext(tail);
        tail.getPrev().setNext(node);
        tail.setPrev(node);
    }

    /**
     * Free the node, remove connections
     * @param node
     */
    private void freeNode(Node<K, V> node){
        node.getPrev().setNext(node.getNext());
        node.getNext().setPrev(node.getPrev());
    }

    /**
     * Free the last node and remove from the map
     */
    private void freeNodeFromTail(){
        Node<K, V> node = tail.getPrev();
        System.out.println("Deleting last node (most recently used) "+node);
        cacheMap.remove(node.getKey());
        freeNode(node);
    }

    @Override
    public int size() {
        return count;
    }
}
