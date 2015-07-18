package com.bbcow.controller;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.MessageHandler;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.bbcow.ServerConfigurator;
import com.bbcow.controller.client.HostConnectCenter;
import com.bbcow.db.MongoPool;
import com.bbcow.po.ShareHost;

/**
 * 访问指定主机
 * 
 * @author 大辉Face
 */
@ServerEndpoint(value = "/open/{path}", configurator = ServerConfigurator.class)
public class OpenController {
        private WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        private Session hostSession = null;

        /**
         * 用户连接
         */
        @OnOpen
        public void open(@PathParam(value = "path") String path, final Session userSession) {
                try {
                	
                		String uri ="";
                		if(DefaultHost.isDefault(path)){
                			uri="ws://localhost:8001/"+path;
                		}else{
                			ShareHost host = MongoPool.findOneHost(path);
                			if(host==null){
                				userSession.getBasicRemote().sendText("{\"type\":0,\"error\":\"未找到主机\"}");
                				return;
                			}
                			uri = "ws://" + host.getIp() + ":" + host.getPort() + "/" + host.getPoint();
                		}
                		
                		hostSession = container.connectToServer(HostConnectCenter.class, new URI(uri));
                        hostSession.addMessageHandler(new MessageHandler.Whole<String>() {
                                @Override
                                public void onMessage(String message) {
                                        try {
                                                userSession.getBasicRemote().sendText(message);
                                        } catch (IOException e) {
                                                e.printStackTrace();
                                        }
                                }
                        });
                        
                } catch (DeploymentException | IOException | URISyntaxException e) {
                        e.printStackTrace();
                }

        }

        /**
         * 用户发送消息
         */
        @OnMessage
        public void userMessage(String message) {
                try {
                        hostSession.getBasicRemote().sendText(message);
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

        @OnClose
        public void close(Session session) {
        }
        

		private enum DefaultHost{
			INDEX("index"),BOX("box"),SHARE("share");
			private String path;
			DefaultHost(String path){
				this.path = path;
			}
			
			public static boolean isDefault(String path){
				for(DefaultHost host : DefaultHost.values()){
					if(host.path.equals(path)){
						return true;
					}
				}
				return false;
			}
		}
}
