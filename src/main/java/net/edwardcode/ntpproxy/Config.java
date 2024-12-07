package net.edwardcode.ntpproxy;

import java.util.HashMap;
import java.util.Map;

public class Config {
    private NTPServer[] ntpServers;

    public NTPServer[] getNtpServers() {
        return ntpServers;
    }

    public void setNtpServer(NTPServer[] ntpServers) {
        this.ntpServers = ntpServers;
    }

    public static class NTPServer {
        private String address;
        private String text;
        private int stratum;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getStratum() {
            return stratum;
        }

        public void setStratum(int stratum) {
            this.stratum = stratum;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public Map<String, Object> getObj() {
            Map<String, Object> data = new HashMap<>();
            data.put("address", address);
            data.put("stratum", stratum);
            data.put("text", text);
            return data;
        }
    }
}
