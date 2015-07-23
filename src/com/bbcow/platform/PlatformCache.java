package com.bbcow.platform;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.websocket.Session;
public class PlatformCache {
        //首页用户
        public static Map<String, Session> userMap = new HashMap<String, Session>();
        //各主机用户
        public static Map<String, List<Session>> hostUserMap = new HashMap<String, List<Session>>();

        public static Map<String, Session> hostMap = new HashMap<String, Session>();

        //随机访问
        public static Queue<String> queue = new LinkedBlockingQueue<String>(5);

       

        
}
