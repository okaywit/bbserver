package com.bbcow.command;

import java.io.IOException;

import javax.websocket.Session;

import com.alibaba.fastjson.JSONObject;
import com.bbcow.db.MongoPool;
import com.bbcow.po.ShareHost;
import com.bbcow.util.RequestParam;

/**
 * 分享主机
 * 
 * @author 大辉Face
 */
public class Command06 implements ICommand {

        @Override
        public void process(String message, Session session) {
                JSONObject object = JSONObject.parseObject(message);
                ShareHost host = new ShareHost();
                host.setIp(object.getString("ip"));
                host.setEmail(object.getString("email"));
                host.setPoint(object.getString("point"));
                host.setPort(object.getString("port"));
                host.setName(object.getString("name"));
                host.setPath(object.getString("path"));
                MongoPool.insertHost(host);

                try {
                        session.getBasicRemote().sendText(RequestParam.returnJson(RequestParam.MESSAGE_TYPE_SUCCESS, "{\"success\":\"分享完成\"}"));
                } catch (IOException e) {
                        e.printStackTrace();
                }

        }
}
