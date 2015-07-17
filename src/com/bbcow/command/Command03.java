package com.bbcow.command;

import java.io.IOException;
import java.util.Iterator;

import javax.websocket.Session;

import com.bbcow.CowCache;
import com.bbcow.CowSession;
import com.bbcow.util.RequestParam;

/**
 * @author 大辉Face
 */
public class Command03 implements ICommand {

        @Override
        public void process(String message, Session session) {
                for (Iterator<CowSession> it = CowCache.cowMap.values().iterator(); it.hasNext();) {
                        try {
                                Session s = it.next().getSession();
                                s.getBasicRemote().sendText(RequestParam.returnJson(RequestParam.MESSAGE_TYPE_CHAT, message));
                        } catch (IOException e) {
                                e.printStackTrace();
                        }
                }
        }

}
