package com.main.cache.test.suite;

import com.main.cache.test.CacheLFUTest;
import com.main.cache.test.CacheLRUTest;
import com.main.cache.test.CacheMFUTest;
import com.main.cache.test.CacheMRUTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({CacheLRUTest.class, CacheMRUTest.class, CacheLFUTest.class, CacheMFUTest.class})

public class TestSuite {

}

