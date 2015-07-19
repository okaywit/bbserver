package com.bbcow.controller;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.atomic.AtomicLong;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.Session;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.bbcow.CowCache;
import com.bbcow.util.BaiduPing;
import com.bbcow.util.BusTask;
import com.bbcow.util.RequestParam;

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
                CowCache.threads.execute(new BusTask(message, session));
                BaiduPing.ping();
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
