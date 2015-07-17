package com.bbcow.command;

import java.io.IOException;

import javax.websocket.Session;

import com.alibaba.fastjson.JSONObject;
import com.bbcow.db.MongoPool;
import com.bbcow.util.RequestParam;

/**
 * 筛选
 * 
 * @author 大辉Face
 */
public class Command05 implements ICommand {

        @Override
        public void process(String message, Session session) {
                try {
                        JSONObject object = JSONObject.parseObject(message);

                        int conditionType = object.getIntValue("type");

                        if (conditionType == RequestParam.MESSAGE_TYPE_YESTERDAY) {
                                for (String s : MongoPool.findYesterday()) {
                                        session.getBasicRemote().sendText(RequestParam.returnJson(RequestParam.MESSAGE_TYPE_YESTERDAY, s));
                                }
                        }
                        if (conditionType == RequestParam.MESSAGE_TYPE_TOP100) {
                                for (String s : MongoPool.findTop100()) {
                                        session.getBasicRemote().sendText(RequestParam.returnJson(RequestParam.MESSAGE_TYPE_TOP100, s));
                                }
                        }

                } catch (IOException e) {
                        e.printStackTrace();
                }
        }
}
