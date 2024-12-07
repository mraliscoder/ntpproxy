package net.edwardcode.ntpproxy;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.*;

@SpringBootApplication
public class NTPProxy {
    public static Config config;
    public static void main(String[] args) throws IOException {
        File conf = new File("config.json");
        if (!conf.exists()) {
            System.err.println("config.json not found");
            return;
        }
        ObjectMapper mapper = new ObjectMapper();
        InputStream configStream = new FileInputStream(conf);
        config = mapper.readValue(configStream, Config.class);

        SpringApplication.run(NTPProxy.class, args);
    }
}
