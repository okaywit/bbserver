package com.bbcow.platform.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.bbcow.BusCache;
import com.bbcow.ServerConfigurator;
import com.bbcow.db.MongoPool;
import com.bbcow.platform.BaiduPing;
import com.bbcow.platform.HostCache;
import com.bbcow.platform.MessageTask;
import com.bbcow.server.po.ShareHost;

/**
 * 访问指定主机
 * 
 * @author 大辉Face
 */
@ServerEndpoint(value = "/open/{path}", configurator = ServerConfigurator.class)
public class OpenController {
        private static WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        private Session hostSession = null;
        private Session userSession = null;

        /**
         * 用户连接
         */
        @OnOpen
        public void userConnect(@PathParam(value = "path") String path, Session userSession) {
                this.userSession = userSession;
                List<Session> ss = HostCache.hostUserMap.get(path);
                if (ss == null) {
                        HostCache.hostUserMap.put(path, ss = new LinkedList<Session>());
                }
                ss.add(userSession);

                ShareHost host = MongoPool.findOneHost(path);
                if (host != null && host.getStatus() == 1) {
                        MongoPool.insertHostTrend(host);
                        String uri = "ws://" + host.getIp() + ":" + host.getPort() + "/" + host.getPoint();
                        try {
                                hostSession = container.connectToServer(new Host(), new URI(uri));
                        } catch (DeploymentException | IOException | URISyntaxException e) {
                                e.printStackTrace();
                        }
                } else {
                        try {
                                userSession.getBasicRemote().sendText("{\"type\":0,\"error\":\"未找到主机\"}");
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }
        }

        @ClientEndpoint
        class Host {

                @OnMessage
                public void hostMessage(String message, Session hostSession) {
                        try {
                                userSession.getBasicRemote().sendText(message);
                                BaiduPing.ping();
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }

                @OnClose
                public void hostQuit(Session hostSession) {
                        try {
                                userSession.close();
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }
        }

        /**
         * 用户发送消息
         */
        @OnMessage
        public void userMessage(@PathParam(value = "path") String path, String message, Session userSession) {
                try {
                        hostSession.getBasicRemote().sendText(message);

                        MessageTask mt = new MessageTask(message, path);
                        BusCache.threads.execute(mt);

                } catch (IOException e) {
                        e.printStackTrace();
                }

        }

        @OnClose
        public void userQuit(@PathParam(value = "path") String path, Session userSession) {
                List<Session> ss = HostCache.hostUserMap.get(path);
                int index = 0;
                for (Session s : ss) {
                        if (s.getId().equals(userSession.getId())) {
                                index = ss.indexOf(s);
                                break;
                        }
                }
                ss.remove(index);

        }

}
