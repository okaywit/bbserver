package com.bbcow.server.controller;

import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.bbcow.BusCache;
import com.bbcow.RequestParam;
import com.bbcow.ServerConfigurator;
import com.bbcow.db.MongoPool;

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
                for (String s : MongoPool.findIndex()) {
                        RequestParam.sendText(BusCache.MESSAGE_TYPE_AD, s, session);
                }
                RequestParam.sendText(BusCache.MESSAGE_TYPE_DAILYMAIN, MongoPool.findMain(), session);
        }

}
