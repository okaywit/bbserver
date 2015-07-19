package com.bbcow.command;

import java.util.LinkedList;
import java.util.List;

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
        public List<String> process(String message, Session session) {
                JSONObject object = JSONObject.parseObject(message);
                ShareHost host = new ShareHost();
                host.setIp(object.getString("ip"));
                host.setEmail(object.getString("email"));
                host.setPoint(object.getString("point"));
                host.setPort(object.getString("port"));
                host.setName(object.getString("name"));
                host.setPath(object.getString("path"));
                
                List<String> list = new LinkedList<String>();
                if(MongoPool.findOneHost(object.getString("path"))!=null){
                    
        			MongoPool.insertHost(host);
        			list.add(RequestParam.returnJson(RequestParam.MESSAGE_TYPE_SUCCESS, "{\"success\":\"分享完成\"}"));
       
        		}else{
        			list.add("{\"type\":0,\"error\":\"英文别名已存在\"}");
        		}
                
                return list;

        }
}
