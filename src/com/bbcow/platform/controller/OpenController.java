package com.bbcow.platform.controller;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.bbcow.ServerConfigurator;
import com.bbcow.platform.HostCache;
import com.bbcow.platform.ProtocolData;

/**
 * 访问指定主机
 * 
 * @author 大辉Face
 */
@ServerEndpoint(value = "/open/{path}", configurator = ServerConfigurator.class)
public class OpenController {
        private Session hostSession = null;

        /**
         * 用户连接
         */
        @OnOpen
        public void userConnect(@PathParam(value = "path") String path, Session userSession) {
                HostCache.userMap.put(userSession.getId(), userSession);

                //检测远程主机是否加载
                while (true) {
                        hostSession = HostCache.hostMap.get(path);
                        if (hostSession == null || !hostSession.isOpen()) {
                                if (HostCache.initHost(path)) {
                                        continue;
                                } else {
                                        return;
                                }
                        }
                        break;
                }

                try {
                        hostSession.getBasicRemote().sendText("{\"sId\":\"" + userSession.getId() + "\"}");
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        /**
         * 用户发送消息
         */
        @OnMessage
        public void userMessage(String message, Session userSession) {
                try {
                        hostSession.getBasicRemote().sendText(new ProtocolData(message, userSession).toString());
                } catch (IOException e) {
                        e.printStackTrace();
                }

        }

        @OnClose
        public void userQuit(Session userSession) {
                HostCache.userMap.remove(userSession.getId());
        }

}
