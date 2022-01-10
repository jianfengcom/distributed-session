package com.imooc.distributedsession.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
public class UserController {
    private static final String LOGIN_USER = "login_user";
    private static final String JWT_KEY = "imooc";

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

    @GetMapping("/jwt/login")
    public String loginWithJwt(@RequestParam String username,
                               @RequestParam String password) {
        Algorithm algorithm = Algorithm.HMAC256(JWT_KEY);
        return JWT.create()
                .withClaim(LOGIN_USER, username)
                .withExpiresAt(new Date(System.currentTimeMillis() + 3600000))
                .sign(algorithm);
    }

    @GetMapping("/jwt/info")
    public String infoWithJwt(@RequestHeader String token) {
        Algorithm algorithm = Algorithm.HMAC256(JWT_KEY);
        JWTVerifier verifier = JWT.require(algorithm).build();
        try {
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getClaim(LOGIN_USER).asString();
        } catch (TokenExpiredException e) {
            // token 过期
        } catch (JWTDecodeException e) {
            // 解码失败，token错误
        }
        return "error";
    }
}
