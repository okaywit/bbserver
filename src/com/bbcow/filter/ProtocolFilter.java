package com.bbcow.filter;

import java.io.IOException;

import com.bbcow.CowCache;
import com.bbcow.command.ICommand;
import com.bbcow.util.NotFindCommandException;

/**
 * 指令检测链-协议
 */
public class ProtocolFilter extends AbstractFilter {

        @Override
        public void filter(Message message) {

                String head = message.originMessage.substring(message.originMessage.indexOf(":") + 1, message.originMessage.indexOf(","));

                message.cId = Integer.parseInt(head);
                ICommand command = CowCache.commandMap.get(message.cId);
                try {
                        if (command == null) {
                                throw new NotFindCommandException(head);
                        }
                        this.nextFilter.filter(message);
                        CowCache.commandMap.get(message.cId).process(message.dealMessage, message.session);
                } catch (NotFindCommandException e) {
                        try {
                                message.session.getBasicRemote().sendText("{\"type\":0,\"error\":\"未找到指令\"}");
                        } catch (IOException e1) {
                                e1.printStackTrace();
                        }
                }

        }

}
