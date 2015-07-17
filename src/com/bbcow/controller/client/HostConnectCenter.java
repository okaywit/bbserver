package com.bbcow.controller.client;

import javax.websocket.ClientEndpoint;
import javax.websocket.OnOpen;
import javax.websocket.Session;

@ClientEndpoint
public class HostConnectCenter {
        @OnOpen
        public void open(Session hostSession) {

        }

}
