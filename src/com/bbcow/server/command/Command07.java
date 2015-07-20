package com.bbcow.server.command;

import java.util.ArrayList;
import java.util.List;

import javax.websocket.Session;

import com.alibaba.fastjson.JSONObject;
import com.bbcow.db.MongoPool;
import com.bbcow.server.util.RequestParam;

/**
 * 推送
 * 
 * @author 大辉Face
 */
public class Command07 implements ICommand {

        @Override
        public List<String> process(String message, Session session) {
                JSONObject object = JSONObject.parseObject(message);
                List<String> list = new ArrayList<String>();
                for (String s : MongoPool.findIndex()) {
                        list.add("{\"sId\":\"" + object.getString("sId") + "\",\"type\":" + RequestParam.MESSAGE_TYPE_AD + ",\"data\":" + s + "}");
                }

                return list;
        }
}
