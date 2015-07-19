package com.bbcow.controller.client;

import java.io.IOException;
import java.util.Iterator;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

import com.bbcow.CowCache;
import com.bbcow.CowSession;
import com.bbcow.util.RequestParam;

@ClientEndpoint
public class HostConnectCenter {
        @OnOpen
        public void hostOpen(Session hostSession) {

        }
        @OnMessage
        public void hostMessage(String message,Session hostSession) {
        	for (Iterator<Session> it = CowCache.userMap.values().iterator(); it.hasNext();) {
                try {
                        Session s = it.next();
                        s.getBasicRemote().sendText(message);
                } catch (IOException e) {
                        e.printStackTrace();
                }
    		}
        }
        
}
