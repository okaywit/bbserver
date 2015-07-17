package com.bbcow;

import java.util.List;

import javax.websocket.Extension;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class ServerConfigurator extends ServerEndpointConfig.Configurator {

        @Override
        public boolean checkOrigin(String originHeaderValue) {

                return super.checkOrigin(originHeaderValue);
        }

        @Override
        public String getNegotiatedSubprotocol(List<String> supported, List<String> requested) {

                return super.getNegotiatedSubprotocol(supported, requested);
        }

        @Override
        public List<Extension> getNegotiatedExtensions(List<Extension> installed, List<Extension> requested) {

                return super.getNegotiatedExtensions(installed, requested);
        }

        @Override
        public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {

                super.modifyHandshake(sec, request, response);
        }

        @Override
        public <T> T getEndpointInstance(Class<T> clazz) throws InstantiationException {

                return super.getEndpointInstance(clazz);
        }

}
