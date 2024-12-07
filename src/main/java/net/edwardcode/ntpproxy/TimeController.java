package net.edwardcode.ntpproxy;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.util.*;

@RestController
@RequestMapping("/")
public class TimeController {
    @GetMapping("/servers")
    public Map<String, Object> getServers() {
        Map<String, Object> servers = new HashMap<>();
        List<Object> items = new ArrayList<>();
        for (Config.NTPServer srv : NTPProxy.config.getNtpServers()) {
            items.add(srv.getObj());
        }
        servers.put("servers", items);
        return servers;
    }

    @GetMapping("/current/{server}")
    public Map<String, Object> getCurrentTime(@PathVariable String server) {
        Map<String, Object> response = new HashMap<String, Object>();
        try {
            long localStartTime = System.currentTimeMillis();

            // Fetch NTP server data
            NTPUDPClient client = new NTPUDPClient();
            client.setDefaultTimeout(10000);

            String ip = null;
            for (Config.NTPServer srv : NTPProxy.config.getNtpServers()) {
                if (Objects.equals(srv.getAddress(), server)) {
                    ip = srv.getAddress();
                    break;
                }
            }
            if (ip == null) {
                response.put("error", "No server with identifier " + server + " found");
                return response;
            }

            InetAddress hostAddr = InetAddress.getByName(ip);
            TimeInfo info = client.getTime(hostAddr);

            long ntpTime = info.getMessage().getTransmitTimeStamp().getTime();
            long localEndTime = System.currentTimeMillis();

            // Calculate jitter
            long rtt = localEndTime - localStartTime;
            long offset = ntpTime - localStartTime;

            long adjustedTime = localStartTime + offset;

            response.put("ntp_time", ntpTime);
            response.put("adjusted_time", adjustedTime);
            response.put("jitter", rtt);
            response.put("offset", offset);
            response.put("unix_time_millis", adjustedTime);
            response.put("address", hostAddr.getHostAddress());
            response.put("host_name", hostAddr.getHostName());
            response.put("stratum", info.getMessage().getStratum());

            client.close();
        } catch (Exception e) {
            response.put("error", "Failed to fetch NTP time: " + e.getMessage());
        }
        return response;
    }
}
