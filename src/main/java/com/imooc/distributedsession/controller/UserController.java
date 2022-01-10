package com.imooc.distributedsession.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final String LOGIN_USER = "login_user";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/login")
    public String login(String username,
                      String password,
                      HttpSession session) {
        session.setAttribute(LOGIN_USER, username);
        return "登录成功!";
    }

    @GetMapping("/info")
    public String login(HttpSession session) {
        return "当前登录的是：" + session.getAttribute(LOGIN_USER);
    }

    @GetMapping("/token/login")
    public String loginWithToken(@RequestParam String username,
                                 @RequestParam String password) {
        String key = "token_" + UUID.randomUUID().toString();
        stringRedisTemplate.opsForValue().set(key, username, 1, TimeUnit.HOURS);
        return key;
    }

    @GetMapping("/token/info")
    public String infoWithToken(@RequestHeader String token) {
        return "当前登录的是：" + stringRedisTemplate.opsForValue().get(token);
    }
}
