package com.bbcow;

import java.util.List;

import javax.websocket.Extension;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class ServerConfigurator extends ServerEndpointConfig.Configurator {

        @Override
        public boolean checkOrigin(String originHeaderValue) {
                System.out.println(originHeaderValue);
                return super.checkOrigin(originHeaderValue);
        }

        @Override
        public String getNegotiatedSubprotocol(List<String> supported, List<String> requested) {
                for (String s : supported) {
                        System.out.println(s);
                }
                return super.getNegotiatedSubprotocol(supported, requested);
        }

        @Override
        public List<Extension> getNegotiatedExtensions(List<Extension> installed, List<Extension> requested) {

                return super.getNegotiatedExtensions(installed, requested);
        }

        @Override
        public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
                /*System.out.println(request.getHeaders());
                System.out.println(sec.getUserProperties());*/
                System.out.println(request.getHttpSession());
                super.modifyHandshake(sec, request, response);
        }

        @Override
        public <T> T getEndpointInstance(Class<T> clazz) throws InstantiationException {
                return super.getEndpointInstance(clazz);
        }

}
