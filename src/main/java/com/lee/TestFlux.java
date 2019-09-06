package com.lee;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

public class TestFlux {
    public static void main(String[] args) {
        Flux.just("foo", "bar", "foobar").doOnNext(e -> {
            System.out.println(e);
            String bb = e + "sda";
            System.out.println(bb);
        }).subscribe(System.out::println);
        Flux.just(322).subscribe(a -> System.out.println(a));

        Flux.fromStream(Arrays.asList("sdsa","3343","df23").stream()).subscribe(System.out::println);

        Flux.empty().subscribe(System.out::println);

        Flux.range(1,10).subscribe(System.out::println);


        Flux.just(1, 2, 3, 4, 5).subscribe(new Subscriber<Integer>() { // 1

            @Override
            public void onSubscribe(Subscription s) {
                System.out.println("onSubscribe");
                s.request(6);   // 2
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("onNext:" + integer);
            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });


    }
}
