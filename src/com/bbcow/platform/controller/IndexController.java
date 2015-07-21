package com.bbcow.platform.controller;

import java.io.IOException;
import java.util.Iterator;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.bbcow.ServerConfigurator;
import com.bbcow.db.MongoPool;
import com.bbcow.platform.HostCache;
import com.bbcow.server.util.RequestParam;

/**
 * 初始化
 * 
 * @author 大辉Face
 */
@ServerEndpoint(value = "/index", configurator = ServerConfigurator.class)
public class IndexController {
        @OnOpen
        public void open(Session userSession) {
                HostCache.userMap.put(userSession.getId(), userSession);

                try {
                        for (String s : MongoPool.findHost()) {
                                userSession.getBasicRemote().sendText(RequestParam.returnJson(RequestParam.MESSAGE_TYPE_SHAREHOST, s));
                        }
                        for (Iterator<String> it = HostCache.queue.iterator(); it.hasNext();) {
                                userSession.getBasicRemote().sendText(it.next());
                        }
                } catch (IOException e) {
                        e.printStackTrace();
                }

        }

        @OnMessage
        public void hostMessage(String message, Session session) {

        }

        @OnClose
        public void userQuit(Session userSession) {
                HostCache.userMap.remove(userSession.getId());
        }
}
