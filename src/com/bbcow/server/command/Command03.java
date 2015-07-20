package com.bbcow.server.command;

import java.util.LinkedList;
import java.util.List;

import javax.websocket.Session;

import com.bbcow.server.util.RequestParam;

/**
 * @author 大辉Face
 */
public class Command03 implements ICommand {

        @Override
        public List<String> process(String message, Session session) {
                List<String> list = new LinkedList<String>();
                list.add(RequestParam.returnJson(RequestParam.MESSAGE_TYPE_CHAT, message));
                return list;
        }

}
