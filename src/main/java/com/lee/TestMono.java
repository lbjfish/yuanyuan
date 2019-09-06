package com.lee;

import javafx.concurrent.Task;
import org.reactivestreams.Subscription;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import org.thymeleaf.util.StringUtils;
import reactor.core.Disposable;
import reactor.core.publisher.*;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.io.Console;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestMono {
    static int a = 5;
    public static void main(String[] args) {
        Mono.just("sdsdsf").delayElement(Duration.ofMillis(100)).doOnNext(e -> {
            System.out.println(e.concat("232"));
        }).subscribe();
        Flux.just("123","456").flatMap(s -> Flux.fromArray(s.split(""))).delayElements(Duration.ofMillis(1))
                .doOnNext(System.out::println).subscribe();

        List<String> words = new ArrayList<String>();
        words.add("hello");
        words.add("word");

        //将words数组中的元素再按照字符拆分，然后字符去重，最终达到["h", "e", "l", "o", "w", "r", "d"]
        //如果使用map，是达不到直接转化成List<String>的结果
        List<String> stringList = words.stream()
                .flatMap(word -> Arrays.stream(word.split("")))
                .collect(Collectors.toList());
        stringList.forEach(e -> System.out.println(e));

        Flux.range(1, 6)    // 1
                .map(i -> i*2).subscribe(System.out::println, System.err::println);   // 2


        Flux.create((t) -> {
            t.next("create");
            t.next("create1");
            t.complete();
        }).subscribe(System.out::println);

        MultiValueMap stringMultiValueMap = new LinkedMultiValueMap<>();
        stringMultiValueMap.add("name","lee");
        stringMultiValueMap.add("name","ppp");
        stringMultiValueMap.add("name","zss");
        stringMultiValueMap.add("ww","dsfdf");
        System.out.println(stringMultiValueMap.getFirst("ww"));

        Set<String> keySet = stringMultiValueMap.keySet();
        for (String key : keySet) {
            List<Object> values = (List<Object>) stringMultiValueMap.get(key);
            System.out.println(key +":"+ values);


        }


        Mono.fromCallable(() -> "create from supplier").subscribe(System.out::println);
        boolean dd = Mono.just(true).block();
        System.out.println(dd == true);


        Flux<String > abc = Flux.just("sda","dsf");
        Mono<List<String>> pps = abc.collectList();
        List<String> sd = pps.block();
        System.out.println(sd);

        Flux<String> flux2 = Flux.just("foo", "chain").flatMap(secret -> Flux.just(secret.replaceAll(".", "*")));
        flux2.subscribe(next -> System.out.println("Received: " + next));

        Flux<String> source = Flux.fromIterable(Arrays.asList("blue", "green", "orange", "purple"))
                .doOnNext(System.out::println)
                .filter(s -> s.startsWith("o"))
                .map(String::toUpperCase);
        source.subscribe(d -> System.out.println("Subscriber 1: "+d));
        source.subscribe(d -> System.out.println("Subscriber 2: "+d));


        StepVerifier.create(Flux.range(1, 7)    // 1
                .map(i -> i * i))   // 2
                .expectNext(1, 4, 9, 16, 25, 36,49)    //3
                .expectComplete()  // 4
                .verify();

        StepVerifier.create(Flux.range(1, 6)   // 1
                .flatMap(s -> Flux.just(s*2))
                .doOnNext(System.out::print))
                .expectNextCount(6)
                .verifyComplete();

        Flux.range(1, 6)   // 1
                .flatMap(s -> Flux.generate(ArrayList::new, (list,sink) -> {
                    int a = s*2;
                    list.add(a);
                    sink.next(a);
                    if(list.size() == 1){
                        sink.complete();
                    }
                    return list;
                })).subscribe(System.out::println);


        Flux.generate(sink -> {
            sink.next("Hello");
            sink.complete();
        }).subscribe(System.out::println);


        final Random random = new Random();
        Flux.generate(ArrayList::new, (list, sink) -> {
            int value = random.nextInt(100);
            list.add(value);
            sink.next(value);
            if (list.size() == 10) {
                sink.complete();
            }
            return list;
        },System.out::println).subscribe(System.out::println);

        Flux.generate(
                () -> 0,
                (state, sink) -> {
                    sink.next("3 x " + state + " = " + 3*state);
                    if (state == 10) sink.complete();
                    return state + 1;
                }).subscribe(System.out::println);

        Flux.generate(
                AtomicLong::new,
                (state, sink) -> {
                    long i = state.getAndIncrement();
                    sink.next("3 x " + i + " = " + 3*i);
                    if (i == 10) sink.complete();
                    return state;
                }, (state) -> System.out.println("state: " + state)).subscribe(System.out::println);

        Flux.create(sink -> {
            sink.next(32);
            sink.next("dsadas");
            sink.complete();
        }).subscribe(System.out::println);


        Mono.fromCompletionStage(CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "s12";
        }).applyToEither(CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "hello world";
        }), s -> s)).subscribe(s -> System.out.println(s));

        Mono.justOrEmpty(Optional.of("fd")).doOnNext(s -> System.out.println(s==null)).subscribe();

        Mono.create(sink -> {
            sink.success(32);
        }).subscribe(System.out::println);



        Flux.range(1, 100).buffer(20).subscribe(System.out::println);

//        Flux.interval(Duration.ofMillis(100)).bufferTimeout(20,Duration.ofMillis(10000)).take(2).toStream()
//                .forEach(p -> {
//                    p.add(1000l);
//                    System.out.println(p);
//                });

        Flux.range(1, 10).bufferUntil(i -> i % 2 == 0).subscribe(System.out::println);

        Flux.range(1, 10).filter(i -> i % 2 == 0).subscribe(System.out::println);


        Flux.just("a", "b")
                .zipWith(Flux.just("c", "d"))
                .subscribe(System.out::println);

        List<String> listdd = Flux.just("a", "b")
                .zipWith(Flux.just("c", "d"), (s1, s2) -> String.format("%s-%s", s1, s2)).collectList().block();
        listdd.add("4343");
        System.out.println(listdd);

        //Flux.range(1, 1000).takeLast(10).subscribe(System.out::println);
        //Flux.range(1, 1000).takeWhile(i -> i < 10).subscribe(System.out::println);
        //Flux.range(1, 1000).takeUntil(i -> i == 10).subscribe(System.out::println);
        Flux.range(1, 100).reduce((x, y) -> x + y).subscribe(System.out::println);
        Flux.range(1, 100).reduceWith(() -> 100, (x, y) -> x + y).subscribe(System.out::println);

        Flux.merge(Flux.range(0,50).take(5), Flux.range(100,150).take(5)).toStream().forEach(System.out::println);
        Flux.just(5,10).flatMap(x -> Flux.just(x*2)).toStream().forEach(System.out::println);
        Flux.combineLatest(
                Arrays::toString,
                Flux.range(0,100).take(5),
                Flux.range(50, 100).take(5)
        ).toStream().forEach(System.out::println);
        Flux.just("das", "2")
                .concatWith(Mono.error(new IllegalStateException()))
                .subscribe(System.out::println, System.err::println);


        Flux.just(1, 2)
                .concatWith(Mono.error(new IllegalStateException()))
                .onErrorReturn(0)
                .subscribe(System.out::println);

        Flux.just(1, 2)
                .concatWith(Mono.error(new IllegalArgumentException()))
                .onErrorResume(e -> {
                    if (e instanceof IllegalStateException) {
                        return Mono.just(0);
                    } else if (e instanceof IllegalArgumentException) {
                        return Mono.just(-1);
                    }
                    return Mono.empty();
                }).subscribe(System.out::println);





        Flux.create(sink -> {
            sink.next(Thread.currentThread().getName());
            sink.complete();
        })
                .publishOn(Schedulers.single())
                .map(x -> String.format("[%s] %s", Thread.currentThread().getName(), x))
                .publishOn(Schedulers.elastic())
                .map(x -> String.format("[%s] %s", Thread.currentThread().getName(), x))
                .subscribeOn(Schedulers.parallel())
                .subscribe(System.out::println);

        Flux.range(1, 2).log("Range").subscribe(System.out::println);

        StepVerifier.create(
                Flux.just("flux", "mono")
                        .flatMap(s -> Flux.fromArray(s.split("\\s*"))   // 1
                                .delayElements(Duration.ofMillis(100))) // 2
                        .doOnNext(System.out::print)) // 3
                .expectNextCount(8) // 4
                .verifyComplete();

        CountDownLatch countDownLatch = new CountDownLatch(1);
        Mono.fromCallable(() -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Hello, Reactor!";
        })    // 1
                .subscribeOn(Schedulers.elastic())  // 2
                .subscribe(System.out::println, null, countDownLatch::countDown);
        try {
            countDownLatch.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


//        Flux.range(1, 6)    // 1
//                .doOnRequest(n -> System.out.println("Request " + n + " values..."))    // 2
//                .subscribe(new BaseSubscriber<Integer>() {  // 3
//                    @Override
//                    protected void hookOnSubscribe(Subscription subscription) { // 4
//                        System.out.println("Subscribed and make a request...");
//                        request(1); // 5
//                    }
//
//                    @Override
//                    protected void hookOnNext(Integer value) {  // 6
//                        try {
//                            TimeUnit.SECONDS.sleep(1);  // 7
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        System.out.println("Get value [" + value + "]");    // 8
//                        request(1); // 9
//                    }
//                });

        Flux<String> alphabet = Flux.just(-1, 13, 9, 20,30)
                .handle((i, sink) -> {
                    String letter = alphabet(i);
                    if (letter != null)
                        sink.next(letter);
                });

        alphabet.subscribe(System.out::println);

        Mono.just(1)
                .publishOn(Schedulers.single())
                .map(a -> a+1)
                .publishOn(Schedulers.elastic())
                .map(b -> b+1)
                .subscribe(System.out::println);


        LongAdder statsCancel = new LongAdder();

                Flux.just("foo", "bar")
                        .doFinally(type -> {
                            if (type == SignalType.CANCEL)

                                statsCancel.increment();
                        })
                        .take(1).subscribe(System.out::println);
        System.out.println(statsCancel);
        Flux<String> flux =
                Flux.interval(Duration.ofMillis(250))
                        .map(input -> {
                            if (input < 3) return "tick " + input;
                            throw new RuntimeException("boom");
                        }).onErrorReturn("error")
                        ;

        flux.subscribe(System.out::println);
        try {
            Thread.sleep(2100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Mono.just(111).concatWith(Mono.just(112)).subscribe(System.out::println);
        Mono.just(22).then(Mono.just(33)).subscribe(System.out::println);

        Flux.range(0, 10)
                //.log()    // 1
                .publishOn(Schedulers.newParallel("myParallel"))
                .log()    // 2
                .subscribeOn(Schedulers.newElastic("myElastic"))
//                .log()    // 3
                .subscribe();//.blockLast();

        Flux.create(sink -> {
            sink.next(Thread.currentThread().getName());
            sink.complete();
        })
                .publishOn(Schedulers.newSingle("newSingle"))
                .publishOn(Schedulers.newElastic("newElastic"))
                .log()
                .subscribeOn(Schedulers.newParallel("new Paraller"))
                .subscribe(System.out::println);


        Flux.just(22, 33)
                .concatWith(Mono.error(new IllegalArgumentException()))
                .retry(1)
                .onErrorResume(e -> {
                    if (e instanceof IllegalStateException) {
                        return Mono.just(0);
                    } else if (e instanceof IllegalArgumentException) {
                        return Mono.just(-2);
                    }
                    return Mono.empty();
                })
                .subscribe(System.out::println);

    }

    public static String alphabet(int letterNumber) {
        if (letterNumber < 1 || letterNumber > 26) {
            return null;
        }
        int letterIndexAscii = 'A' + letterNumber - 1;
        return "" + (char) letterIndexAscii;
    }
}
