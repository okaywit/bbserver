package com.bbcow.command;

import java.util.List;

import javax.websocket.Session;

import com.alibaba.fastjson.JSONObject;
import com.bbcow.db.MongoPool;

/**
 * @author 大辉Face
 */
public class Command02 implements ICommand {

        @Override
        public List<String> process(String message, Session session) {
                JSONObject object = JSONObject.parseObject(message);
                int type = object.getIntValue("type");
                long id = object.getLongValue("id");
                if (type == 1)
                        MongoPool.doLike(id);
                if (type == 0)
                        MongoPool.doNotLike(id);
                MongoPool.insertPaperTrend(id, type);
                return null;
        }

}
