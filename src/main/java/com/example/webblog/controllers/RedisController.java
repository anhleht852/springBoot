package com.example.webblog.controllers;

import com.example.webblog.servies.BaseRedis;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class RedisController {

    private  final BaseRedis baseRedis;

    @PostMapping
    public void set(){
        baseRedis.set("hihi", "aa");
    }
}
