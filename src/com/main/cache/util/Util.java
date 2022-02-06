package com.main.cache.util;

import org.junit.runner.Description;

public class Util {
    public static void logStartingTestCase(Description description){
        System.out.println("*********************** START "
                + description.getTestClass().getSimpleName()
                + "." + description.getMethodName()
                + "***********************");
    }

    public static void logFinishedTestCase(Description description){
        System.out.println("*********************** END "
                + description.getTestClass().getSimpleName()
                + "." + description.getMethodName()
                + "***********************");
    }
}
