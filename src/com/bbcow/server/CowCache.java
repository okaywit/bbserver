package com.bbcow.server;

import java.util.HashMap;
import java.util.Map;

import com.bbcow.server.command.Command01;
import com.bbcow.server.command.Command02;
import com.bbcow.server.command.Command03;
import com.bbcow.server.command.Command04;
import com.bbcow.server.command.Command05;
import com.bbcow.server.command.ICommand;

/**
 * 缓存
 * 
 * @author 大辉Face
 */

public class CowCache {

        public static Map<Integer, ICommand> commandMap = new HashMap<Integer, ICommand>();

        public static void init() {

                commandMap.put(1, new Command01());
                commandMap.put(2, new Command02());
                commandMap.put(3, new Command03());
                commandMap.put(4, new Command04());
                commandMap.put(5, new Command05());

        }

}
