package com.bbcow.filter;

import javax.websocket.Session;

/**
 * 指令检测链
 */
public abstract class AbstractFilter {
        protected AbstractFilter nextFilter;

        public void setNextFilter(AbstractFilter nextFilter) {
                this.nextFilter = nextFilter;
        }

        public abstract void filter(Message message);

        public static void startChain(String message, Session session) {
                AbstractFilter protocolFilter = new ProtocolFilter();
                AbstractFilter messageFilter = new MessageFilter();

                protocolFilter.setNextFilter(messageFilter);
                protocolFilter.filter(new Message(message, session));
        }

        static class Message {
                int cId;
                String originMessage;
                String dealMessage;
                Session session;

                Message(String message, Session session) {
                        this.originMessage = message;
                        this.session = session;
                }

        }
}
