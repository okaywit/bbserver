package com.bbcow.controller;

import java.io.IOException;

import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.bbcow.ServerConfigurator;
import com.bbcow.db.MongoPool;
import com.bbcow.util.RequestParam;

/**
 * 分享
 * 
 * @author 大辉Face
 */
@ServerEndpoint(value = "/share", configurator = ServerConfigurator.class)
public class ShareController extends AbstractController {
        @OnOpen
        @Override
        public void open(Session session) {

                try {
                        for (String s : MongoPool.findHost()) {
                                session.getBasicRemote().sendText(RequestParam.returnJson(RequestParam.MESSAGE_TYPE_SHAREHOST, s));
                        }
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }
}
