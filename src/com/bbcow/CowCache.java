package com.bbcow;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.bbcow.command.Command01;
import com.bbcow.command.Command02;
import com.bbcow.command.Command03;
import com.bbcow.command.Command04;
import com.bbcow.command.Command05;
import com.bbcow.command.Command06;
import com.bbcow.command.ICommand;

/**
 * 缓存
 * 
 * @author 大辉Face
 */

public class CowCache {
        public static ExecutorService threads = null;
        public static Map<String, CowSession> cowMap = new HashMap<String, CowSession>();
        public static Map<Integer, ICommand> commandMap = new HashMap<Integer, ICommand>();

        public static void init() {
                commandMap.put(1, new Command01());
                commandMap.put(2, new Command02());
                commandMap.put(3, new Command03());
                commandMap.put(4, new Command04());
                commandMap.put(5, new Command05());
                commandMap.put(6, new Command06());

                threads = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        }
}
