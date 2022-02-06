package com.main.cache.test;

import com.main.cache.Cache;
import com.main.cache.EvictionPolicy;
import com.main.cache.util.Util;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class CacheMRUTest {

    @Rule
    public TestWatcher watchman= new TestWatcher() {
        @Override
        protected void starting(Description description) {
            Util.logStartingTestCase(description);
        }

        @Override
        protected void finished(Description description) {
            Util.logFinishedTestCase(description);
        }
    };

    @Test
    /**
     * Case : Make the cache full and try inserting one more element
     * Result : Most recently element entered should be evicted
     */
    public void testOneElementMoreThanCapacity() {
        Cache<Integer, Integer> cacheMRU = Cache.create(EvictionPolicy.MRU, 3);
        cacheMRU.put(1, 10);
        cacheMRU.put(2, 20);
        cacheMRU.put(3, 30);
        cacheMRU.put(4, 40);
        Assert.assertEquals("First element should not be present",null, cacheMRU.get(3));
    }

    @Test
    /**
     * Case : Make the cache full, get first element and try inserting one more element
     * Result : Second element should be evicted
     */
    public void testGetFirstElement() {
        Cache<Integer, Integer> cacheMRU = Cache.create(EvictionPolicy.MRU, 3);
        cacheMRU.put(1, 10);
        cacheMRU.put(2, 20);
        cacheMRU.put(3, 30);
        cacheMRU.get(1);
        cacheMRU.put(4, 40);
        Assert.assertEquals("First element should be evicted", null, cacheMRU.get(1));
        Assert.assertEquals("Second element should be present", Integer.valueOf(20), cacheMRU.get(2));
    }

    @Test
    /**
     * Case : Make the cache full, and try inserting more elements
     * Result : Size should be equal to capacity
     */
    public void testSizeInsertMoreThanCapacity() {
        Cache<Integer, Integer> cacheMRU = Cache.create(EvictionPolicy.MRU, 3);
        cacheMRU.put(1, 10);
        cacheMRU.put(2, 20);
        cacheMRU.put(3, 30);
        cacheMRU.put(4, 40);
        cacheMRU.put(5, 50);
        cacheMRU.put(6, 60);

        Assert.assertEquals("Size should be equal to capacity", 3, cacheMRU.size());
    }

    @Test
    /**
     * Case : Try inserting null value
     * Result : Throws Exception, as null value is not allowed
     */
    public void testInsertNullValue() {
        Cache<Integer, Integer> cacheMRU = Cache.create(EvictionPolicy.MRU, 3);
        Assert.assertThrows(IllegalArgumentException.class,() -> cacheMRU.put(1,null));
    }

    @Test
    /**
     * Case : Try fetching the key that does not exist
     * Result : null value
     */
    public void testGetKeyNotPresentInCache() {
        Cache<Integer, Integer> cacheMRU = Cache.create(EvictionPolicy.MRU, 3);
        cacheMRU.put(1, 10);
        cacheMRU.put(2, 20);
        cacheMRU.put(3, 30);
        Assert.assertEquals("Not present", null,  cacheMRU.get(4));
    }
}
