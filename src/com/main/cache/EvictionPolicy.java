package com.main.cache;

public enum EvictionPolicy {
    LRU(1), // Least recently used
    MRU(2), // Most recently used
    LFU(3), // Least frequently used
    MFU(4)  // Most frequently used
    ;

    int code;
    EvictionPolicy(int code) {
        this.code = code;
    }

    public static EvictionPolicy getByCode(int code){
        for(EvictionPolicy evictionPolicy : EvictionPolicy.values()){
            if(evictionPolicy.code==code){
                return evictionPolicy;
            }
        }
        throw new IllegalArgumentException("Wrong option");
    }
}
