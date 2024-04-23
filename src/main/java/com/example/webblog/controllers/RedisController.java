package com.example.webblog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("/redis-test")
    public String testRedisConnection() {
        try {
            // Ghi một giá trị vào Redis
            redisTemplate.opsForValue().set("hs", "hasaa");
            // Đọc giá trị từ Redis
            String value = redisTemplate.opsForValue().get("test-key");
            // Trả về thông báo kết nối thành công cùng với giá trị đọc được từ Redis
            return "Redis connection is successful. Value retrieved from Redis: " + value;
        } catch (Exception e) {
            // Trả về thông báo lỗi nếu có lỗi xảy ra
            return "Error: " + e.getMessage();
        }
    }
    @GetMapping("/check-key")
    public String checkKey(@RequestParam("key") String key) {
        Boolean exists = redisTemplate.hasKey(key);
        if (exists) {
            String value = redisTemplate.opsForValue().get(key);
            return "Key exists. Value: " + value;
        } else {
            return "Key does not exist.";
        }
    }
}