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

import com.bbcow.CowCache;
import com.bbcow.ServerConfigurator;
import com.bbcow.controller.client.HostConnectCenter;
import com.bbcow.db.MongoPool;
import com.bbcow.po.ShareHost;
import com.bbcow.util.RequestParam;

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
        public void userConnect(@PathParam(value = "path") String path, final Session userSession) {
        	//TODO 加载一个list,来初始化存储所有在线服务器的数据,定时更新
        	CowCache.userMap.put(userSession.getId(), userSession);
        	//检测远程主机是否加载
        	while(true){
        		hostSession = CowCache.hostMap.get(path);
	            if(hostSession==null){
	            	if(CowCache.initHost(path)){
	            		continue;
	            	}else{
	            		//TODO 发送错误信息
	            		return;
	            	}
	            }
	            break;
        	}

        }

        /**
         * 用户发送消息
         */
        @OnMessage
        public void userMessage(String message,Session userSession) {
                try {
					hostSession.getBasicRemote().sendText(message);
				} catch (IOException e) {
					e.printStackTrace();
				}
                
        }

        @OnClose
        public void userQuit(Session userSession) {
        	CowCache.userMap.remove(userSession.getId());
        }
        /*

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
		}*/
}
