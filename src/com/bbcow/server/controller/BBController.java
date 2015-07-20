package com.bbcow.server.controller;

import java.io.IOException;

import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import com.alibaba.fastjson.JSONObject;
import com.bbcow.ServerConfigurator;
import com.bbcow.db.MongoPool;
import com.bbcow.server.po.Paper;
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

        }

        @OnMessage
        @Override
        public void message(String message, Session session) {
                JSONObject object = JSONObject.parseObject(message);
                String sId = object.getString("sId");
                JSONObject data = object.getJSONObject("data");
                if (data == null) {
                        try {
                                for (String s : MongoPool.findIndex()) {
                                        session.getBasicRemote().sendText(RequestParam.returnJson(RequestParam.MESSAGE_TYPE_AD, sId, s));
                                }
                                for (String s : MongoPool.findHost()) {
                                        session.getBasicRemote().sendText(RequestParam.returnJson(RequestParam.MESSAGE_TYPE_SHAREHOST, sId, s));
                                }

                                session.getBasicRemote().sendText(RequestParam.returnJson(RequestParam.MESSAGE_TYPE_DAILYMAIN, sId, MongoPool.findMain()));
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                } else {
                        Paper paper = new Paper();
                        paper.setId(System.currentTimeMillis());
                        paper.setContactName(data.getString("contactName"));
                        paper.setContactTel(data.getString("contactTel"));
                        paper.setContent(data.getString("content"));
                        paper.setTag(data.getString("tag"));
                        paper.setTitle(data.getString("title"));
                        paper.setImgUrl(data.getString("imgUrl"));
                        paper.setLinkUrl(data.getString("linkUrl"));
                        paper.setBadCount(0);
                        paper.setGoodCount(0);

                        MongoPool.insertPaper(paper);
                }
        }
}
