package com.main.cache.test;

import com.main.cache.Cache;
import com.main.cache.EvictionPolicy;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class CacheLFUTest {

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
        Cache<Integer, Integer> cacheLFU = Cache.create(EvictionPolicy.LFU, 3);
        cacheLFU.put(1, 10);
        cacheLFU.put(2, 20);
        cacheLFU.put(3, 30);
        cacheLFU.put(4, 40);
        Assert.assertEquals("First element should not be present",null, cacheLFU.get(1));
    }

    @Test
    /**
     * Case : Make the cache full, get first element and try inserting one more element
     * Result : Second element should be evicted
     */
    public void testGetFirstElement() {
        Cache<Integer, Integer> cacheLFU = Cache.create(EvictionPolicy.LFU, 3);
        cacheLFU.put(1, 10);
        cacheLFU.put(2, 20);
        cacheLFU.put(3, 30);
        cacheLFU.get(1);
        cacheLFU.put(4, 40);
        Assert.assertEquals("First element should be present", Integer.valueOf(10), cacheLFU.get(1));
        Assert.assertEquals("Second element should be evicted", null, cacheLFU.get(2));
    }

    @Test
    /**
     * Case : Make the cache full, and try inserting more elements
     * Result : Size should be equal to capacity
     */
    public void testSizeInsertMoreThanCapacity() {
        Cache<Integer, Integer> cacheLFU = Cache.create(EvictionPolicy.LFU, 3);
        cacheLFU.put(1, 10);
        cacheLFU.put(2, 20);
        cacheLFU.put(3, 30);
        cacheLFU.put(4, 40);
        cacheLFU.put(5, 50);
        cacheLFU.put(6, 60);

        Assert.assertEquals("Size should be equal to capacity", 3, cacheLFU.size());
    }

    @Test
    /**
     * Case : Try inserting null value
     * Result : Throws Exception, as null value is not allowed
     */
    public void testInsertNullValue() {
        Cache<Integer, Integer> cacheLFU = Cache.create(EvictionPolicy.LFU, 3);
        Assert.assertThrows(IllegalArgumentException.class,() -> cacheLFU.put(1,null));
    }

    @Test
    /**
     * Case : Try fetching the key that does not exist
     * Result : null value
     */
    public void testGetKeyNotPresentInCache() {
        Cache<Integer, Integer> cacheLFU = Cache.create(EvictionPolicy.LFU, 3);
        cacheLFU.put(1, 10);
        cacheLFU.put(2, 20);
        cacheLFU.put(3, 30);
        Assert.assertEquals("Not present", null,  cacheLFU.get(4));
    }

    @Test
    /**
     * Case : Increase frequency of all elements (cache full) to 2
     * get one element to make its frequency 3 and insert another element
     * Result : The one which was used last should be evicted
     */
    public void testIncreaseFreqAndAdd() {
        Cache<Integer, Integer> cacheLFU = Cache.create(EvictionPolicy.LFU, 3);
        cacheLFU.put(1, 10);
        cacheLFU.put(2, 20);
        cacheLFU.put(3, 30);
        cacheLFU.get(2);
        cacheLFU.get(3);
        cacheLFU.get(1);
        cacheLFU.get(2);
        cacheLFU.put(4, 40);
        Assert.assertEquals("should not be present", null,  cacheLFU.get(3));
    }

    @Test
    /**
     * Case : Increase frequency of all elements (cache full) to 2
     * put one element to make its frequency 3 and insert another element
     * Result : The one which was used last should be evicted
     */
    public void testIncreaseFreqViaPutAndAdd() {
        Cache<Integer, Integer> cacheLFU = Cache.create(EvictionPolicy.LFU, 3);
        cacheLFU.put(1, 10);
        cacheLFU.put(2, 20);
        cacheLFU.put(3, 30);
        cacheLFU.get(2);
        cacheLFU.get(3);
        cacheLFU.get(1);
        cacheLFU.put(2, 200);
        cacheLFU.put(4, 40);
        Assert.assertEquals("should not be present", null,  cacheLFU.get(3));
        Assert.assertEquals("Value should match", Integer.valueOf(200),  cacheLFU.get(2));
        Assert.assertEquals("Value should match", 3,  cacheLFU.size());
    }

}
