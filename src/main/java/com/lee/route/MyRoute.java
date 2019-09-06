package com.lee.route;

import com.lee.handler.MyHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;

@Configuration
public class MyRoute {

    @Autowired
    private MyHandler myHandler;

    @Bean
    public RouterFunction<ServerResponse> timerRouter() {
        return RouterFunctions.route(GET("/time")
                .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), myHandler::getTime
        );
    }
}
