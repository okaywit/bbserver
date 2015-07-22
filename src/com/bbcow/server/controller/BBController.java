package com.bbcow.server.controller;

import java.io.IOException;

import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.bbcow.ServerConfigurator;
import com.bbcow.db.MongoPool;
import com.bbcow.server.util.RequestParam;

/**
 * 消息
 * 
 * @author 大辉Face
 */
@ServerEndpoint(value = "/bb", configurator = ServerConfigurator.class)
public class BBController extends AbstractController {
        @OnOpen
        @Override
        public void open(Session session) {
                try {
                        for (String s : MongoPool.findIndex()) {
                                session.getBasicRemote().sendText(RequestParam.returnJson(RequestParam.MESSAGE_TYPE_AD, s));
                        }

                        session.getBasicRemote().sendText(RequestParam.returnJson(RequestParam.MESSAGE_TYPE_DAILYMAIN, MongoPool.findMain()));
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }

}
