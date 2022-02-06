package com.main.cache.test;

import com.main.cache.Cache;
import com.main.cache.EvictionPolicy;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class CacheLRUTest {

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
        Cache<Integer, Integer> cacheLRU = Cache.create(EvictionPolicy.LRU, 3);
        cacheLRU.put(1, 10);
        cacheLRU.put(2, 20);
        cacheLRU.put(3, 30);
        cacheLRU.put(4, 40);
        Assert.assertEquals("First element should not be present",null, cacheLRU.get(1));
    }

    @Test
    /**
     * Case : Make the cache full, get first element and try inserting one more element
     * Result : Second element should be evicted
     */
    public void testGetFirstElement() {
        Cache<Integer, Integer> cacheLRU = Cache.create(EvictionPolicy.LRU, 3);
        cacheLRU.put(1, 10);
        cacheLRU.put(2, 20);
        cacheLRU.put(3, 30);
        cacheLRU.get(1);
        cacheLRU.put(4, 40);
        Assert.assertEquals("First element should be present", Integer.valueOf(10), cacheLRU.get(1));
        Assert.assertEquals("Second element should be evicted", null, cacheLRU.get(2));
    }

    @Test
    /**
     * Case : Make the cache full, and try inserting more elements
     * Result : Size should be equal to capacity
     */
    public void testSizeInsertMoreThanCapacity() {
        Cache<Integer, Integer> cacheLRU = Cache.create(EvictionPolicy.LRU, 3);
        cacheLRU.put(1, 10);
        cacheLRU.put(2, 20);
        cacheLRU.put(3, 30);
        cacheLRU.put(4, 40);
        cacheLRU.put(5, 50);
        cacheLRU.put(6, 60);

        Assert.assertEquals("Size should be equal to capacity", 3, cacheLRU.size());
    }

    @Test
    /**
     * Case : Try inserting null value
     * Result : Throws Exception, as null value is not allowed
     */
    public void testInsertNullValue() {
        Cache<Integer, Integer> cacheLRU = Cache.create(EvictionPolicy.LRU, 3);
        Assert.assertThrows(IllegalArgumentException.class,() -> cacheLRU.put(1,null));
    }

    @Test
    /**
     * Case : Try fetching the key that does not exist
     * Result : null value
     */
    public void testGetKeyNotPresentInCache() {
        Cache<Integer, Integer> cacheLRU = Cache.create(EvictionPolicy.LRU, 3);
        cacheLRU.put(1, 10);
        cacheLRU.put(2, 20);
        cacheLRU.put(3, 30);
        Assert.assertEquals("Not present", null,  cacheLRU.get(4));
    }

}
