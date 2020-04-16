package com.lee;

import com.google.gson.Gson;
import com.lee.testspringboot2.po.User;
import com.lee.util.NumberValidationUtil;
import com.lee.vo.LeeStu;
import com.lee.vo.Stu;
import com.lee.vo.Stuson;
import com.lee.vo.Stutwoson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.reactivestreams.Subscription;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.*;
import reactor.core.scheduler.Schedulers;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuple3;
import reactor.util.function.Tuples;

import java.math.BigDecimal;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static reactor.util.context.Context.of;

@RunWith(SpringRunner.class)
@SpringBootTest
public class YuanyuanApplicationTests {
    private List<String> df;

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
        Mono.fromSupplier(() -> {
            List<String> aa = Arrays.asList("aa","vv","cc");
            return aa;
        }).doOnNext(s -> s.forEach(a -> {
            System.out.println(a);
            int g = 22;
            System.out.println(g);
        })).doOnNext(s -> s.forEach(a -> System.out.println(a+"111"))).subscribe(System.out::println);
    }

    @Test
    public void test9(){
        Flux<String> source = Flux.fromIterable(Arrays.asList("blue", "green", "orange", "purple"))
                .doOnNext(System.out::println).doOnNext(p -> System.out.println(p+"==="));
        source.subscribe();

        Map<String,Object> map = new HashMap<>(2);
        map.put("id",11);
        map.put("name","dsaf");
        map.put("age",3243);
        System.out.println(map);
    }

    @Test
    public void test10(){
        Stutwoson stu = new Stutwoson();
        stu.setPp(223);
        stu.setSonName("dkjfdk");
        if(stu instanceof Stuson){
            Stuson stuson = stu;
            System.out.println(stuson.getSonName());
        }
        System.out.println(stu instanceof Stutwoson);
        Byte aa = 11;
        byte bb = 11;
        System.out.println(Objects.equals(aa,bb));

        Function<Integer, Integer> name = e -> e * 2;
        Function<Integer, Integer> square = e -> e * e;
        int value = name.andThen(square).apply(3);
        System.out.println("andThen value=" + value);
        int value2 = name.compose(square).apply(3);
        System.out.println("compose value2=" + value2);
    }

    @Test
    public void test11(){
        Tuple3<Integer, String, Integer> base = Tuples.of(100, "Foo",453);
        System.out.println(base.getT1());
        System.out.println(base.getT2());
        System.out.println(base.getT3());

        String abc = "abc123";
        System.out.println(abc.length());

        String beforeTrimUid = String.valueOf(12345L);
        String afterTrimUid = beforeTrimUid.substring(beforeTrimUid.length()-2, beforeTrimUid.length());
        System.out.println(afterTrimUid);
    }

    Tuple3<Integer, String, Integer> abc(Integer a, String b, Integer c){
        return Tuples.of(100, "Foo",453);
    }

    @Test
    public void test12(){
        Map<Double,Object> map = new HashMap<>();
        map.put(0.58,2);
        map.put(0.27,5);
        map.put(0.88,2);
        double key = map.entrySet().stream().min(Map.Entry.comparingByKey()).get().getKey();
        Object vv = map.entrySet().stream().min(Map.Entry.comparingByKey()).get().getValue();
        System.out.println(key);
        System.out.println(vv);
    }

    @Test
    public void test13(){
        // 实例化
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
// 为name放入3个值
        params.add("name", "Name参数1");
        params.add("name", "Name参数2");
        params.add("name", "Name参数3");
// 打印第三个参数
        System.out.println(params.get("name").get(2));

        // 放入id一个idList
        List idList = new ArrayList();
        idList.add("123");
        idList.add("456");
        idList.add("789");
        params.put("id", idList);
// 打印第三个参数
        System.out.println(params.get("id").get(2));

        // 打印所有值
        Set<String> keySet = params.keySet();
        for (String key : keySet) {
            List<String> values = ((LinkedMultiValueMap<String, String>) params).get(key);
            for (String value : values) {
                System.out.println(key + ": " + value);
            }
        }
    }

    @Test
    public void test14(){
        List<Integer> list = Arrays.asList(1,2,3,4,5);
        List<Integer> list2 = list.stream().filter(in -> (!Objects.equals(in,2)) && (!Objects.equals(in,3))).collect(Collectors.toList());
        System.out.println(list2);

        String str = "232";
        System.out.println();

        String ss = "abcd";
        String s2 = ss.substring(ss.length()-2,ss.length());
        System.out.println(s2);

        Map<String,Object> map = new HashMap<>();
        Map<String,Map<String,Object>> map2 = new HashMap<>();
        map2.put("aa",map);
        System.out.println(map2.get("aa").containsKey("abc"));
    }

    @Test
    public void test15(){
       //Flux.just(22, 33, 44, 55).doOnNext(System.out::println).doOnNext(System.out::println).subscribe();
        String abc = "abcd";
        List<String> list = Arrays.asList("abc","def","ddd","fff","ggg");
        list.stream().filter(x-> !Objects.equals(x,"ddd")).forEach(System.out::println);
    }

    @Test
    public void test16(){
        List<User> list = Collections.EMPTY_LIST;
        list = Arrays.asList(new User(11,"aaa"),new User(22,"bbb"),new User(33,"ccc"));
        System.out.println(list);
        List<Integer> list2 = list.stream().map(User::getId).collect(Collectors.toList());
        System.out.println(list2);
        List<Integer> list3 = list.stream().mapToInt(User::getId).boxed().collect(Collectors.toList());
        System.out.println(list3);
        Byte a = 22;
        Long b = a.longValue();
        System.out.println(a instanceof Byte);
        System.out.println(b instanceof Long);


        Integer maxId = list.stream().map(rule -> Optional.ofNullable(rule.getId()).orElse(Integer.valueOf("0"))).max(Integer::compareTo).orElse(Integer.valueOf("0"));
        System.out.println(maxId);
        Integer maxId2 = list.stream().map(rule -> Optional.ofNullable(rule.getId()).orElse(Integer.valueOf("0"))).max(Integer::compareTo).orElse(Integer.valueOf("0"));
        System.out.println(maxId2);

    }

    @Test
    public void test17(){
        Stuson stuson = new Stuson();
        stuson.setName("lee");
        stuson.setAge(22);
        stuson.setDate(new Date());
        stuson.setSonName("son");
        ModelMapper modelMapper = new ModelMapper();
        Stu dto = modelMapper.map(stuson,Stu.class);
        System.out.println(dto.getName());
        System.out.println(dto.getAge());
        System.out.println(dto.getDate());
        System.out.println(dto.getB());

        Gson gson = new Gson();
        String str = gson.toJson(stuson);
        System.out.println(str);
    }

    @Test
    public void test18(){
        LeeStu stuson = new LeeStu();
        stuson.setName("lee");
        stuson.setAge(22);
        stuson.setDate(new Date());
        stuson.setSonName("son");
        Gson gson = new Gson();
        String str = gson.toJson(stuson);
        System.out.println(str);

        Stuson dto = gson.fromJson(str, Stuson.class);
        System.out.println(dto.getName());
        System.out.println(dto.getAge());
        System.out.println(dto.getDate());
        System.out.println(dto.getB());

        List<String> aa = new ArrayList<>();
        aa.add("11");
        System.out.println(aa.size());
        System.out.println(aa);
        List<String> bb = Arrays.asList("aa","bb","cc");
        aa.addAll(bb);
        System.out.println(aa.size());
        System.out.println(aa);

        List<String> dd = new ArrayList<>();
        System.out.println(dd.isEmpty());

        System.out.println(df.isEmpty());

    }

    @Test
    public void test19(){
        Mono.just(true).doOnNext(x -> System.out.println(x)).subscribe();
        String abc;
        abc = "dad";
        System.out.println(abc);
        Map<String,Object> map = new HashMap<>();
        map.put("aa",11);
        map.put("bb",22);
        int size = map.entrySet().size();
        System.out.println(size);
        for(Iterator<Map.Entry<String, Object>> it = map.entrySet().iterator();it.hasNext();){
            Map.Entry<String, Object> map2 = it.next();
            if(Objects.equals(map2.getValue(), 11)){
                it.remove();
            }
        }
        System.out.println(map);

        List<String> list23 = null;
        System.out.println(list23.isEmpty());
    }

    @Test
    public void test20(){
        Map<String,Long> map = new HashMap<>();
        map.put("def",1111l);
        System.out.println(map.get("abc"));
        System.out.println(map.get("def"));
        long dd = 432423l;
        System.out.println(dd > map.get("def"));
        System.out.println(dd > map.get("abc"));
    }

    @Test
    public void test21(){
        List<Integer> list = new ArrayList<>(Arrays.asList(1,3,2,5,6,8));
        boolean dd = list.removeIf(x -> x == 4);
        System.out.println(list);
        System.out.println(dd);
    }

    @Test
    public void test22(){

        String dateTimeStr = "2019-06-14";
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dateTime2 = LocalDate.parse(dateTimeStr, df);
        System.out.println(dateTime2);
        int year = dateTime2.getYear();
        int month = dateTime2.getMonth().getValue();
        String month2 = String.valueOf(dateTime2.getMonth().getValue());
        if(month < 10){
            month2 = "0" + month;
        }
        int day = dateTime2.getDayOfMonth();
        System.out.println(day + "/" + month2 + "/" + year);

        Collection<String> collection = new ArrayList<>();
        collection.add("aaa");
        collection.add("bbbb");
        collection.add("ccc");
        System.out.println(collection);

        LocalDate date = LocalDate.now();
        System.out.println(date);
        LocalDate newDate = date.plus(3, ChronoUnit.DAYS);
        System.out.println("newDate=" + newDate);
        Instant instant = Instant.now();
        long i1 = instant.toEpochMilli();
        System.out.println(instant);
        System.out.println(i1);
        System.out.println(System.currentTimeMillis() + "---");
        Instant instant2 = instant.plus(3, ChronoUnit.DAYS);
        long i2 = instant2.toEpochMilli();
        System.out.println(instant2);
        System.out.println(i2);

        LocalDateTime dsf = instant2.atOffset(ZoneOffset.of("+8")).toLocalDateTime();
        System.out.println(dsf);
        LocalDateTime ds = instant2.atOffset(ZoneOffset.of("+7")).toLocalDateTime();
        System.out.println(ds);

        LocalDateTime l1 = Instant.ofEpochMilli(1577592655987L).atOffset(ZoneOffset.of("+7")).toLocalDateTime();
        LocalDateTime l2 = Instant.ofEpochMilli(1577592655987L).atOffset(ZoneOffset.of("+8")).toLocalDateTime();
        System.out.println(l1);
        System.out.println(l2);

        LocalDate localDate = LocalDate.parse("2019-12-25");
        System.out.println(localDate);
        System.out.println(localDate.plusDays(3).toString()+"ppp");

        LocalDate fds = Instant.now().plus(3, ChronoUnit.DAYS).atOffset(ZoneOffset.of("+7")).toLocalDate();
        System.out.println(fds+"rrr");


        Map<String,Object> map = new HashMap<>();
        map.put("abc",123);
        map.put("vvv",555.12);
        System.out.println(map);
        String abc = map.get("abc").toString();
        String bbb = map.get("vvv").toString();
        System.out.println(abc);
        System.out.println(bbb);
        Double dds = Double.parseDouble(abc);
        System.out.println(dds);
        Double sd = 0D;
        System.out.println(sd.toString());

        DateTimeFormatter df2 = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String bbsa = fds.format(df2);
        System.out.println(bbsa);

        Double sda = Double.parseDouble("-5000");
        System.out.println(sda);

        Mono.just(1).map(x -> x*2).zipWhen(booking -> {
           return Mono.just(23+booking);
        }).subscribe(System.out::println);

        Mono.zip(Mono.just(1), Mono.just(2), Mono.just(5)).map(tuple2 -> {
            return tuple2.getT1()+tuple2.getT2()+ tuple2.getT3();
        }).subscribe(System.out::println);


        Map<String,Object> mapw = new HashMap<>();
        Integer aaa = Optional.ofNullable((Integer) mapw.get("ads")).orElse(0);
        System.out.println(aaa);

        double i = 399 * 0.075;
        System.out.println(i);
        Double aa = 399D;
        Double bb = 0.075D;
        double dss = new BigDecimal(aa).multiply(new BigDecimal(bb.toString())).doubleValue();
        System.out.println(dss);

        double cc = 182.6;
        System.out.println(new BigDecimal(cc));


        BigDecimal a=new BigDecimal(Double.toString(12.0));
        BigDecimal b=new BigDecimal(Double.toString(11.9));
        System.out.println(a.subtract(b).toString());   //返回值为String

        System.out.println(12.0D-11.9D);      //返回值是float

        String sbd = "www.lee.com";
        String sbd3 = "www.lee.com";
        String sbd2 = "lee";
        System.out.println(sbd3.startsWith(sbd));
        System.out.println(sbd2.startsWith(sbd));

        List<Integer> list232 = Arrays.asList(1,2,3);
        List<Integer> list233 = Arrays.asList(4,5,2);
        Set<Integer> set1 = new HashSet<>();
        set1.addAll(list232);
        Set<Integer> set2 = new HashSet<>();
        set2.addAll(list233);
        boolean fdf = set1.retainAll(set2);
        System.out.println(fdf);
        System.out.println(set1);
        System.out.println(set2);

        System.out.println(Tuples.of(true, new Stuson()));

        List<Stu> list = new ArrayList<>();
        list.add(new Stu("aaa",23));
        list.add(new Stu("bbb",24));
        list.add(new Stu("ccc",25));
        Stu stu = list.stream().max(Comparator.comparingInt(Stu::getAge)).get();
        System.out.println(stu);
    }
}
