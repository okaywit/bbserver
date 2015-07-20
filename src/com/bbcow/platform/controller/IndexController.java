package com.bbcow.platform.controller;

import java.io.IOException;
import java.util.Iterator;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.bbcow.ServerConfigurator;
import com.bbcow.platform.HostCache;
import com.bbcow.platform.ProtocolData;

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

                for (Iterator<Session> it = HostCache.hostMap.values().iterator(); it.hasNext();) {
                        Session hostSession = it.next();
                        if (hostSession == null || !hostSession.isOpen()) {
                                continue;
                        }
                        ProtocolData data = new ProtocolData("{\"cId\":7,\"data\":{}}", userSession);
                        try {
                                hostSession.getBasicRemote().sendText(data.toString());
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }

        }

        @OnMessage
        public void hostMessage(String message, Session session) {
                /*try {
                        session.getBasicRemote().sendText(new ProtocolData(message, session).toString());
                } catch (IOException e) {
                        e.printStackTrace();
                }*/
        }

        @OnClose
        public void userQuit(Session userSession) {
                HostCache.userMap.remove(userSession.getId());
        }
}
