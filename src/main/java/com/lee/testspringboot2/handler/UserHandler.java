package com.lee.testspringboot2.handler;

import com.lee.testspringboot2.po.User;
import com.lee.testspringboot2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class UserHandler {
    @Autowired
    private UserRepository userRepository;

    public Mono<ServerResponse> findById(ServerRequest request){
        int id = Integer.parseInt(request.pathVariable("id"));
        User user = userRepository.findById(id);
        Mono<User> mono = Mono.just(user);
        return ServerResponse.ok().body(mono, User.class);
    }
}
