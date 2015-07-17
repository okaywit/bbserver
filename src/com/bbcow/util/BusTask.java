package com.bbcow.util;

import javax.websocket.Session;

import com.bbcow.filter.AbstractFilter;

public class BusTask implements Runnable {
        private String message;
        private Session session;

        public BusTask(String message, Session session) {
                this.message = message;
                this.session = session;
        }

        @Override
        public void run() {
                AbstractFilter.startChain(this.message, this.session);
        }

}
