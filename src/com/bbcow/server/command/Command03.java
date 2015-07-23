package com.bbcow.server.command;

import java.util.LinkedList;
import java.util.List;

import javax.websocket.Session;

import com.alibaba.fastjson.JSONObject;
import com.bbcow.BusCache;
import com.bbcow.RequestParam;

/**
 * @author 大辉Face
 */
public class Command03 implements ICommand {

        @Override
        public List<String> process(String message, Session session) {
                JSONObject object = JSONObject.parseObject(message);
                List<String> list = new LinkedList<String>();
                list.add(RequestParam.returnJson(BusCache.MESSAGE_TYPE_CHAT, object.getString("chatMessage")));
                return list;
        }

}
