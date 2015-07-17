package com.bbcow.controller;

import java.io.IOException;

import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.bbcow.CowCache;
import com.bbcow.CowSession;
import com.bbcow.ServerConfigurator;
import com.bbcow.db.MongoPool;
import com.bbcow.util.RequestParam;

/**
 * 消息
 * 
 * @author 大辉Face
 */
@ServerEndpoint(value = "/index", configurator = ServerConfigurator.class)
public class BBController extends AbstractController {
        @OnOpen
        @Override
        public void open(Session session) {
                long index = cowIndex.getAndIncrement();
                CowCache.cowMap.put(session.getId(), new CowSession(index, session));

                try {
                        for (String s : MongoPool.findIndex()) {
                                session.getBasicRemote().sendText(RequestParam.returnJson(RequestParam.MESSAGE_TYPE_AD, s));
                        }
                        for (String s : MongoPool.findHost()) {
                                session.getBasicRemote().sendText(RequestParam.returnJson(RequestParam.MESSAGE_TYPE_SHAREHOST, s));
                        }

                        session.getBasicRemote().sendText(RequestParam.returnJson(RequestParam.MESSAGE_TYPE_DAILYMAIN, MongoPool.findMain()));
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }
}
