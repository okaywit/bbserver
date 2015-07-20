package com.bbcow.server.po;

public class ShareHost {
        private String ip;
        private String port;
        private String point;
        private String email;
        private String name;
        private int status;
        private int type;
        private String path;

        public String getIp() {
                return ip;
        }

        public void setIp(String ip) {
                this.ip = ip;
        }

        public String getPort() {
                return port;
        }

        public void setPort(String port) {
                this.port = port;
        }

        public String getPoint() {
                return point;
        }

        public void setPoint(String point) {
                this.point = point;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public int getStatus() {
                return status;
        }

        public void setStatus(int status) {
                this.status = status;
        }

        public int getType() {
                return type;
        }

        public void setType(int type) {
                this.type = type;
        }

        public String getPath() {
                return path;
        }

        public void setPath(String path) {
                this.path = path;
        }

}
