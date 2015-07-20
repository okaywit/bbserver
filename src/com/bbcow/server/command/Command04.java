package com.bbcow.server.command;

import java.util.LinkedList;
import java.util.List;

import javax.websocket.Session;

import com.alibaba.fastjson.JSONObject;
import com.bbcow.db.MongoPool;
import com.bbcow.server.util.RequestParam;

/**
 * @author 大辉Face
 */
public class Command04 implements ICommand {

        @Override
        public List<String> process(String message, Session session) {
                JSONObject object = JSONObject.parseObject(message);
                String fakeName = object.getString("fakeName");
                //过滤 ## 标示
                String paperId = object.getString("paperId").replaceAll("#", "");

                String ad = MongoPool.findOne(Long.parseLong(paperId));

                List<String> list = new LinkedList<String>();
                list.add(RequestParam.returnJson(RequestParam.MESSAGE_TYPE_PUSH, ad));
                return list;
        }

}
