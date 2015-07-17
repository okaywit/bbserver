package com.bbcow.controller;

import java.io.IOException;

import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.bbcow.ServerConfigurator;

/**
 * 分享
 * 
 * @author 大辉Face
 */
@ServerEndpoint(value = "/news", configurator = ServerConfigurator.class)
public class NewsController extends AbstractController {
        @OnOpen
        @Override
        public void open(Session session) {

                try {
                        session.getBasicRemote().sendText("window.open('http://www.baidu.com')");
                } catch (IOException e) {
                        e.printStackTrace();
                }
        }
}
