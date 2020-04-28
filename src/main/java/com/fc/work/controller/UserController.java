package com.fc.work.controller;

import com.fc.common.Utils.UUIDUtils;
import com.fc.common.annotation.JSONR;
import com.fc.common.service.IdService;
import com.fc.work.exception.LoginException;
import com.fc.work.pojo.HeaderConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/user")
public class UserController {

    private final static String userName_dataBase = "admin";

    private final static String password_dataBase = "admin";

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private IdService idService;

    @PostMapping(value = "/login")
    public String login(String userName, String password, HttpServletResponse response)  {

        System.out.println("========================>"+idService.getId());
        //登陆请求 验证用户名密码
        if(!userName_dataBase.equals(userName)){
            throw new LoginException("用户名不存在");
        }
        if(!password_dataBase.equals(password)){
            throw new LoginException("密码不正确");
        }
        //随机产生一个token
        String token  = UUIDUtils.getUUID();
        //写到header中
        Cookie cookie = new Cookie( HeaderConstant.HEARD_KEY,token);
        cookie.setPath("/");

        cookie.setMaxAge(3600*24*3);

        response.addCookie(cookie);

        //过期时间一分钟
        stringRedisTemplate.opsForValue().set(token,userName,60, TimeUnit.SECONDS);
        return "client/index";
    }
}
