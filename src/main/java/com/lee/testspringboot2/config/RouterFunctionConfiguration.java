package com.lee.testspringboot2.config;

import com.lee.testspringboot2.handler.UserHandler;
import com.lee.testspringboot2.po.User;
import com.lee.testspringboot2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 路由器函数 配置
 */
@Configuration
public class RouterFunctionConfiguration {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserHandler userHandler;

    /**
     * 此接口解释：
     * 回顾一下以前的Servlet：
     *  请求接口： ServletRequest 或者 HttpServletRequest
     *  响应接口： ServletResponse 或者 HttpServletResponse
     *
     * Spring5.0提供 重新定义了服务请求和响应接口：（就是不用老的Servlet那一套了）
     *  请求接口： ServerRequest
     *  响应接口： ServerResponse
     *  好处：即可支持Servlet规范， 也可以支持自定义，比如Netty (Web Server)
     *
     * 以本例：
     *  定义一个Get请求， 并且返回所有的用户对象 URI: /person/find/all
     *
     * Flux 是 0 - N 个对象集合
     * Mono 是 0 - 1 个对象集合
     * Reactive中Flux或者Mono是异步处理（非阻塞处理）
     * 以前（不用flux或Mono老方法）是同步处理 （阻塞处理）
     *
     * Flux 或者 Mono 都是 Publisher
     */
    @Bean
    public RouterFunction<ServerResponse> personFindAll(){
        return RouterFunctions.route(RequestPredicates.GET("/person/find/all"), request -> {
            //返回所有对象
            List<User> users = userRepository.findAll();

            Flux<User> userFlux = Flux.fromIterable(users);

            Mono<ServerResponse> response = ServerResponse.ok().body(userFlux, User.class);
            return response;
        });
    }

    /**
     * 上面那个和这个其实可以写成一个（就是都放这里，所有请求在handler里完成，这里包含所有请求就行），
     * 但是上面那个是慕课网的，所以我就不改了，具体可以看这个链接：
     *      https://blog.csdn.net/qq_28423433/article/details/81221933
     * @return
     */
    @Bean
    public RouterFunction<ServerResponse> routersFunction(){
        RouterFunction<ServerResponse> routerFunction = RouterFunctions
                .route(RequestPredicates.GET("/person/find/{id}"), userHandler::findById);
        return routerFunction;
    }
}
