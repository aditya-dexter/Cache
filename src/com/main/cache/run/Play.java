package com.main.cache.run;

import com.main.cache.Cache;
import com.main.cache.EvictionPolicy;
import com.main.cache.test.suite.TestSuite;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

import java.util.Scanner;

public class Play {
    public static void main(String args[]){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select cache type..");
        System.out.println("1. Least Recently Used (LRU)");
        System.out.println("2. Most Recently Used (MRU)");
        System.out.println("3. Least Frequently Used (LFU)");
        System.out.println("4. Most Frequently Used (MFU)");
        System.out.println("9. To execute all JUnits");

        int code = scanner.nextInt();

        if(code == 9){
            Result result= JUnitCore.runClasses(TestSuite.class);
            System.exit(0);
        }

        System.out.println("Select capacity");

        int capacity = scanner.nextInt();
        EvictionPolicy evictionPolicy = EvictionPolicy.getByCode(code);
        Cache<Integer, Integer> cache = Cache.create(evictionPolicy, capacity);

        System.out.println("<Integer, Integer> "+ evictionPolicy+" Cache created, please select an action");

        while(true){
            System.out.println("1. Put element");
            System.out.println("2. Get element");
            System.out.println("0. Exit");

            int action = scanner.nextInt();

            if(action == 1){
                System.out.print("Insert key=");
                int key = scanner.nextInt();
                System.out.print("Insert value=");
                int value = scanner.nextInt();
                cache.put(key,value);
            }

            if(action == 2){
                System.out.print("Get key=");
                int key = scanner.nextInt();
                cache.get(key);
            }

            if(action == 0){
                System.exit(0);
            }
        }

    }
}
