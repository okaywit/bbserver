package com.bbcow.server.command;

import java.util.LinkedList;
import java.util.List;

import javax.websocket.Session;

import com.alibaba.fastjson.JSONObject;
import com.bbcow.BusCache;
import com.bbcow.RequestParam;
import com.bbcow.db.MongoPool;

/**
 * @author 大辉Face
 */
public class Command04 implements ICommand {

        @Override
        public List<String> process(String message, Session session) {
                JSONObject object = JSONObject.parseObject(message);
                object = object.getJSONObject("chatMessage");
                //过滤 ## 标示
                String paperId = object.getString("paperId").replaceAll("#", "");

                String ad = MongoPool.findOne(Long.parseLong(paperId));

                List<String> list = new LinkedList<String>();
                list.add(RequestParam.returnJson(BusCache.MESSAGE_TYPE_PUSH, ad));
                return list;
        }

}
