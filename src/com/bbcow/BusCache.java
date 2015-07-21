package com.bbcow;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BusCache {
        public static ExecutorService threads = null;
        static {
                threads = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        }

}
