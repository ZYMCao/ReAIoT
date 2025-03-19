package cn.easttrans.reaiot.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Author: ZhanyingCao
 * @CreateDate: 2025/3/18 18:03
 **/
@Controller
public class WebController {
    @GetMapping("/")
    public String chatPage() {
        return "chat";
    }
}
