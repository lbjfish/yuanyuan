package com.lee.testspringboot2.controller;

import com.lee.YuanController;
import com.lee.testspringboot2.po.User;
import com.lee.testspringboot2.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/person/save")
    public User save(String name){
        User user = new User();
        user.setName(name);
        boolean isSave = userRepository.save(user);
        if(isSave){
            logger.info("用户对象：{} 保存成功", user);
        }
        return user;
    }

    @GetMapping("/person/find/all")
    public List<User> findAll(){
        return userRepository.findAll();
    }

    @GetMapping("/person/find/{id}")
    public User findById(@PathVariable("id") int id){
        return userRepository.findById(id);
    }

    @PostMapping("/hello")
    public Mono<String> hello(String name) {   // 【改】返回类型为Mono<String>
        return Mono.just("Welcome to reactive world ~" + name);     // 【改】使用Mono.just生成响应式数据
    }
}
