package com.bbcow.server;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.bbcow.server.command.Command01;
import com.bbcow.server.command.Command02;
import com.bbcow.server.command.Command03;
import com.bbcow.server.command.Command04;
import com.bbcow.server.command.Command05;
import com.bbcow.server.command.Command06;
import com.bbcow.server.command.Command07;
import com.bbcow.server.command.ICommand;

/**
 * 缓存
 * 
 * @author 大辉Face
 */

public class CowCache {
        public static ExecutorService threads = null;
        public static Map<Integer, ICommand> commandMap = new HashMap<Integer, ICommand>();

        public static void init() {

                commandMap.put(1, new Command01());
                commandMap.put(2, new Command02());
                commandMap.put(3, new Command03());
                commandMap.put(4, new Command04());
                commandMap.put(5, new Command05());
                commandMap.put(6, new Command06());
                commandMap.put(7, new Command07());

                threads = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        }

}
