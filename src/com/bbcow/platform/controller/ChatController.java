package com.bbcow.platform.controller;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.bbcow.RequestParam;
import com.bbcow.ServerConfigurator;
import com.bbcow.platform.PlatformCache;

/**
 * 聊天
 * 
 * @author 大辉Face
 */
@ServerEndpoint(value = "/chat", configurator = ServerConfigurator.class)
public class ChatController {
        @OnOpen
        public void open(Session session) {
                PlatformCache.userMap.put(session.getId(), session);
        }

        @OnMessage
        public void chatMessage(String message, Session session) {

                RequestParam.sendChat(message);

        }

        @OnClose
        public void chatQuit(Session session) {
                PlatformCache.userMap.remove(session.getId());
        }
}