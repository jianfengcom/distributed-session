package com.imooc.distributedsession.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final String LOGIN_USER = "login_user";

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
}
