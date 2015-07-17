package com.bbcow.command;

import java.io.IOException;
import java.util.Iterator;

import javax.websocket.Session;

import com.alibaba.fastjson.JSONObject;
import com.bbcow.CowCache;
import com.bbcow.CowSession;
import com.bbcow.db.MongoPool;
import com.bbcow.util.RequestParam;

/**
 * @author 大辉Face
 */
public class Command04 implements ICommand {

        @Override
        public void process(String message, Session session) {
                JSONObject object = JSONObject.parseObject(message);
                String fakeName = object.getString("fakeName");
                //过滤 ## 标示
                String paperId = object.getString("paperId").replaceAll("#", "");

                String ad = MongoPool.findOne(Long.parseLong(paperId));
                for (Iterator<CowSession> it = CowCache.cowMap.values().iterator(); it.hasNext();) {
                        try {
                                Session s = it.next().getSession();
                                s.getBasicRemote().sendText(RequestParam.returnJson(RequestParam.MESSAGE_TYPE_PUSH, ad));
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }
        }

}
