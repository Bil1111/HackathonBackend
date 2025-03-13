package com.example.config.ip;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class IpInfoService {

    @Value("${ipinfo.api.token}")
    private String apiToken;

    @Autowired
    private RestTemplate restTemplate;


    private static final String IPINFO_URL = "https://ipinfo.io/{ip}?token={token}";

    public Map<String, Object> getIpInfo(String ip) {
        return restTemplate.getForObject(IPINFO_URL, Map.class, ip, apiToken);
    }

    public Map<String, Object> getIpInfo(HttpServletRequest request) {
        String ip = extractClientIp(request);
        return getIpInfo(ip);
    }

    private String extractClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // Якщо IP є локальним, спробуємо отримати зовнішній IP
        if ("0:0:0:0:0:0:0:1".equals(ip) || "127.0.0.1".equals(ip)) {
            ip = getPublicIp();
        }

        return ip.split(",")[0];
    }

    private String getPublicIp() {
        try {
            return new RestTemplate().getForObject("https://api64.ipify.org?format=json", Map.class).get("ip").toString();
        } catch (Exception e) {
            return "UNKNOWN_IP";
        }
    }

}