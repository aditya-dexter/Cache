package com.main.cache.test;

import com.main.cache.Cache;
import com.main.cache.EvictionPolicy;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class CacheMFUTest {

    @Rule
    public TestWatcher watchman= new TestWatcher() {
        @Override
        protected void starting(Description description) {
            System.err.println("Starting > " + description.getTestClass().getSimpleName() + "." + description.getMethodName());
        }

        @Override
        protected void finished(Description description) {
            System.err.println("Finished > " + description.getTestClass().getSimpleName() + "." + description.getMethodName());
        }
    };

    @Test
    /**
     * Case : Make the cache full and try inserting one more element
     * Result : First element entered should be evicted
     */
    public void testOneElementMoreThanCapacity() {
        Cache<Integer, Integer> cacheMFU = Cache.create(EvictionPolicy.MFU, 3);
        cacheMFU.put(1, 10);
        cacheMFU.put(2, 20);
        cacheMFU.put(3, 30);
        cacheMFU.put(4, 40);
        Assert.assertEquals("First element should not be present",null, cacheMFU.get(1));
    }

    @Test
    /**
     * Case : Make the cache full, get first element and try inserting one more element
     * Result : First element should be evicted
     */
    public void testGetFirstElement() {
        Cache<Integer, Integer> cacheMFU = Cache.create(EvictionPolicy.MFU, 3);
        cacheMFU.put(1, 10);
        cacheMFU.put(2, 20);
        cacheMFU.put(3, 30);
        cacheMFU.get(1);
        cacheMFU.put(4, 40);
        Assert.assertEquals("First element should not be present", null, cacheMFU.get(1));
        Assert.assertEquals("Second element should be present", Integer.valueOf(20), cacheMFU.get(2));
    }

    @Test
    /**
     * Case : Make the cache full, and try inserting more elements
     * Result : Size should be equal to capacity
     */
    public void testSizeInsertMoreThanCapacity() {
        Cache<Integer, Integer> cacheMFU = Cache.create(EvictionPolicy.MFU, 3);
        cacheMFU.put(1, 10);
        cacheMFU.put(2, 20);
        cacheMFU.put(3, 30);
        cacheMFU.put(4, 40);
        cacheMFU.put(5, 50);
        cacheMFU.put(6, 60);

        Assert.assertEquals("Size should be equal to capacity", 3, cacheMFU.size());
    }

    @Test
    /**
     * Case : Try inserting null value
     * Result : Throws Exception, as null value is not allowed
     */
    public void testInsertNullValue() {
        Cache<Integer, Integer> cacheMFU = Cache.create(EvictionPolicy.MFU, 3);
        Assert.assertThrows(IllegalArgumentException.class,() -> cacheMFU.put(1,null));
    }

    @Test
    /**
     * Case : Try fetching the key that does not exist
     * Result : null value
     */
    public void testGetKeyNotPresentInCache() {
        Cache<Integer, Integer> cacheMFU = Cache.create(EvictionPolicy.MFU, 3);
        cacheMFU.put(1, 10);
        cacheMFU.put(2, 20);
        cacheMFU.put(3, 30);
        Assert.assertEquals("Not present", null,  cacheMFU.get(4));
    }

    @Test
    /**
     * Case : Increase frequency of all elements (cache full) to 2
     * get one element to make its frequency 3 and insert another element
     * Result : Result : The one with frequency 3 should be evicted
     */
    public void testIncreaseFreqAndAdd() {
        Cache<Integer, Integer> cacheMFU = Cache.create(EvictionPolicy.MFU, 3);
        cacheMFU.put(1, 10);
        cacheMFU.put(2, 20);
        cacheMFU.put(3, 30);
        cacheMFU.get(1);
        cacheMFU.get(2);
        cacheMFU.get(3);
        cacheMFU.get(2);
        cacheMFU.put(4, 40);
        Assert.assertEquals("should not be present", null,  cacheMFU.get(2));
    }

    @Test
    /**
     * Case : Increase frequency of all elements (cache full) to 2
     * put one element to make its frequency 3 and insert another element
     * Result : The one with frequency 3 should be evicted
     */
    public void testIncreaseFreqViaPutAndAdd() {
        Cache<Integer, Integer> cacheMFU = Cache.create(EvictionPolicy.MFU, 3);
        cacheMFU.put(1, 10);
        cacheMFU.put(2, 20);
        cacheMFU.put(3, 30);
        cacheMFU.get(2);
        cacheMFU.get(3);
        cacheMFU.get(1);
        cacheMFU.put(2, 200);
        cacheMFU.put(4, 40);
        Assert.assertEquals("should not be present", null,  cacheMFU.get(2));
        Assert.assertEquals("Value should match", Integer.valueOf(40),  cacheMFU.get(4));
        Assert.assertEquals("Value should match", 3,  cacheMFU.size());
    }

}
