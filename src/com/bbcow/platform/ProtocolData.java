package com.bbcow.platform;

import javax.websocket.Session;

import com.alibaba.fastjson.JSONObject;

public class ProtocolData {
        private String cId;
        private String sId;
        private String data;

        public ProtocolData(String json, Session session) {
                JSONObject object = JSONObject.parseObject(json);
                this.sId = session.getId();
                this.data = object.getString("data");
        }

        public String getcId() {
                return cId;
        }

        public void setcId(String cId) {
                this.cId = cId;
        }

        public String getsId() {
                return sId;
        }

        public void setsId(String sId) {
                this.sId = sId;
        }

        public String getData() {
                return data;
        }

        public void setData(String data) {
                this.data = data;
        }

        @Override
        public String toString() {
                return "{\"cId\":" + this.cId + ",\"sId\":\"" + this.sId + "\",\"data\":" + data + "}";
        }

}
