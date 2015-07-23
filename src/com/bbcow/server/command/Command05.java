package com.bbcow.server.command;

import java.util.LinkedList;
import java.util.List;

import javax.websocket.Session;

import com.alibaba.fastjson.JSONObject;
import com.bbcow.BusCache;
import com.bbcow.db.MongoPool;

/**
 * 筛选
 * 
 * @author 大辉Face
 */
public class Command05 implements ICommand {

        @Override
        public List<String> process(String message, Session session) {
                List<String> list = new LinkedList<String>();
                JSONObject object = JSONObject.parseObject(message);
                int conditionType = object.getIntValue("conditionType");
                if (conditionType == BusCache.MESSAGE_TYPE_YESTERDAY) {
                        list.addAll(MongoPool.findYesterday());
                }
                if (conditionType == BusCache.MESSAGE_TYPE_TOP100) {
                        list.addAll(MongoPool.findTop100());
                }

                return list;
        }
}
