package com.fc.work.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class IndexController {

    @RequestMapping("/index")
    public String index( ) {
        return "client/index";
    }

    @RequestMapping("/login")
    public String login() {

        return "client/login";
    }
}