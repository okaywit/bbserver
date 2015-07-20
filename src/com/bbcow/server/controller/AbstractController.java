package com.bbcow.server.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.Session;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.alibaba.fastjson.JSONObject;
import com.bbcow.server.CowCache;

/**
 * 中央控制器
 * 
 * @author 大辉Face
 */
public abstract class AbstractController {
        protected volatile static AtomicLong cowIndex = new AtomicLong();
        private static Logger logger = Logger.getLogger(AbstractController.class);

        static {
                try {
                        PropertyConfigurator.configure(new URL("http://127.0.0.1/log4j.properties"));
                } catch (MalformedURLException e) {
                        e.printStackTrace();
                }
        }

        public abstract void open(Session session);

        @OnMessage
        public void message(String message, Session session) {
                JSONObject object = JSONObject.parseObject(message);
                List<String> list = CowCache.commandMap.get(object.getInteger("cId")).process(message, session);

                for (String msg : list) {
                        try {
                                session.getBasicRemote().sendText(msg);
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }
        }

        @OnClose
        public void close(Session session) {
                logger.info(session.getId() + " go away! ");
        }

        @OnError
        public void error(Throwable t) {
                t.printStackTrace();
        }

}
