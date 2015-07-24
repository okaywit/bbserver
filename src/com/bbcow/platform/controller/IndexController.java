package com.bbcow.platform.controller;

import java.util.Iterator;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;

import com.bbcow.BusCache;
import com.bbcow.ServerConfigurator;
import com.bbcow.db.MongoPool;
import com.bbcow.platform.PlatformCache;
import com.bbcow.util.RequestParam;

/**
 * 初始化
 * 
 * @author 大辉Face
 */
@ServerEndpoint(value = "/index", configurator = ServerConfigurator.class)
public class IndexController {
        private static Logger log = Logger.getLogger(IndexController.class);

        @OnOpen
        public void open(Session userSession) {
                PlatformCache.userMap.put(userSession.getId(), userSession);
                long t1 = System.currentTimeMillis();

                for (String s : MongoPool.findHost()) {
                        RequestParam.sendText(BusCache.MESSAGE_TYPE_SHAREHOST, s, userSession);
                }
                for (String s : MongoPool.findPaperTrend()) {
                        RequestParam.sendText(BusCache.MESSAGE_TYPE_AD, s, userSession);
                }
                for (Iterator<String> it = PlatformCache.queue.iterator(); it.hasNext();) {
                        RequestParam.sendText(BusCache.MESSAGE_TYPE_PUSH, it.next(), userSession);
                }

                log.error("Loading index.html used " + (+System.currentTimeMillis() - t1) + " millions");
        }

        @OnMessage
        public void indexMessage(String message, Session session) {

        }

        @OnClose
        public void indexQuit(Session session) {
                PlatformCache.userMap.remove(session.getId());
        }
}
