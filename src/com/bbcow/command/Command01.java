package com.bbcow.command;

import java.io.IOException;
import java.util.Iterator;

import javax.websocket.Session;

import com.alibaba.fastjson.JSONObject;
import com.bbcow.CowCache;
import com.bbcow.CowSession;
import com.bbcow.db.MongoPool;
import com.bbcow.po.Paper;
import com.bbcow.util.RequestParam;

/**
 * @author 大辉Face
 */
public class Command01 implements ICommand {

        @Override
        public void process(String message, Session session) {
                JSONObject object = JSONObject.parseObject(message);
                Paper paper = new Paper();
                paper.setId(System.currentTimeMillis());
                paper.setContactName(object.getString("contactName"));
                paper.setContactTel(object.getString("contactTel"));
                paper.setContent(object.getString("content"));
                paper.setTag(object.getString("tag"));
                paper.setTitle(object.getString("title"));
                paper.setImgUrl(object.getString("imgUrl"));
                paper.setLinkUrl(object.getString("linkUrl"));
                paper.setBadCount(0);
                paper.setGoodCount(0);

                MongoPool.insertPaper(paper);

                for (Iterator<CowSession> it = CowCache.cowMap.values().iterator(); it.hasNext();) {
                        try {
                                Session s = it.next().getSession();
                                s.getBasicRemote().sendText(RequestParam.returnJson(RequestParam.MESSAGE_TYPE_AD, JSONObject.toJSONString(paper)));
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }

        }

}
