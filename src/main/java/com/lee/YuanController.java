package com.lee;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class YuanController {
    private static final Logger logger = LoggerFactory.getLogger(YuanController.class);

    public YuanController(){
        System.out.println("daskjdakls skdjaskldskj山东矿机");
    }

    @GetMapping(value = "/yuanyuan")
    public String say(){
        logger.info("张三李四忘了圣诞快乐静安寺恐龙当家三！都说了空间大史莱克232s");
        return "index";
    }

    public static void main(String[] args) {

    }
}
