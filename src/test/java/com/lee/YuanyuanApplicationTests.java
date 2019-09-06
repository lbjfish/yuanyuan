package com.lee;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.reactivestreams.Subscription;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Hooks;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.test.publisher.PublisherProbe;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;

@RunWith(SpringRunner.class)
@SpringBootTest
public class YuanyuanApplicationTests {

    @Test
    public void contextLoads() {
        WebClient webClient = WebClient.builder().baseUrl("http://localhost:8809").build();   // 1
        Mono<String> resp = webClient
                .get().uri(s -> {
                    return s.path("/hello").queryParam("name","abc代付款i觉得").build();
                }) // 2
                .retrieve() // 3
                .bodyToMono(String.class);  // 4
        resp.subscribe(System.out::println);    // 5
        try {
            TimeUnit.SECONDS.sleep(1);  // 6
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test1(){
        Flux.create(sink -> {
            sink.next(Thread.currentThread().getName());
            sink.complete();
        })
                .publishOn(Schedulers.newSingle("newSingle"))
                .publishOn(Schedulers.newElastic("newElastic"))
                .subscribeOn(Schedulers.newParallel("new Paraller"))
                .log()
                .subscribe();
        while (true){}

    }

    @Test
    public void test2(){
        Flux.range(1, 10)
                .parallel(2)
                .runOn(Schedulers.parallel())
//                .publishOn(Schedulers.parallel())
                .log()
                .subscribe();

        try {
            TimeUnit.MILLISECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test3(){
        Flux.first(Flux.range(1,5).flatMap(x -> Flux.just(x*2))).subscribe(System.out::println);

        Flux.just(22, 33)
                .concatWith(Mono.error(new IllegalArgumentException()))
                .onErrorResume(e -> Flux.error(new IllegalArgumentException("dsakljdaks"))).doFinally(a -> {
            System.out.println(a+"5552222");
        })
                .subscribe(System.out::println);
    }

    @Test
    public void test4(){
        Function<Flux<String>, Flux<String>> filterAndMap =
                f -> f.filter(color -> !color.equals("orange"))
                        .map(String::toUpperCase);

        Flux<String> transformFlux = Flux.fromIterable(Arrays.asList("blue", "green", "orange", "purple"))
                .doOnNext(System.out::println)
                .transform(filterAndMap);
        transformFlux.subscribe(d -> System.out.println("Subscriber to Transformed MapAndFilter: "+d));

    }

    @Test
    public void test5(){
        AtomicInteger ai = new AtomicInteger();
        Function<Flux<String>, Flux<String>> filterAndMap = f -> {
            if (ai.incrementAndGet() == 1) {
                return f.filter(color -> !color.equals("orange"))
                        .map(String::toUpperCase);
            }
            return f.filter(color -> !color.equals("purple"))
                    .map(String::toUpperCase);
        };

        Flux<String> composedFlux =
                Flux.fromIterable(Arrays.asList("blue", "green", "orange", "purple"))
                        .doOnNext(System.out::println)
                        .compose(filterAndMap);

        composedFlux.subscribe(d -> System.out.println("Subscriber 1 to Composed MapAndFilter :" + d));
        composedFlux.subscribe(d -> System.out.println("Subscriber 2 to Composed MapAndFilter: " + d));
    }

    @Test
    public void test6(){
        Mono.just(22).thenEmpty(Mono.empty()).subscribe(System.out::println);

    }

    public static <T> Function<Mono<T>, Mono<String>> monoResultMap() {
        return mono -> mono.filter(color -> !color.equals("orange"))
                .map(x-> x+"=lbj");

    }

    @Test
    public void test7(){
        Hooks.onOperatorDebug();
        Flux.just(22, 33)
                .concatWith(Mono.error(new IllegalArgumentException("woquni")))
                .checkpoint("rrrrrrrrrrrrrrrrrrrrrrr")
                .subscribe(System.out::println);
    }

    @Test
    public void test8(){
        Mono.fromSupplier(() -> Mono.just(2)).doOnNext(s -> System.out.println(s)).subscribe(System.out::println);
    }

}
