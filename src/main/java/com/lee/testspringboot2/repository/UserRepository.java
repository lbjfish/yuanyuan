package com.lee.testspringboot2.repository;

import com.lee.testspringboot2.po.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 目前只是测试，所以不用数据库，这个repository是模拟有数据库的serviceImpl
 */
@Repository
public class UserRepository {

    //不用数据库就用这种内存方式存储数据
    private Map<Integer, User> repository = new HashMap<>();

    //用java代码模拟数据库id自增
    private final static AtomicInteger idGenerator = new AtomicInteger();

    public List<User> findAll(){
        List<User> list = new ArrayList<>();
        User user = new User();
        user.setId(idGenerator.incrementAndGet());
        user.setName("zhangsan");

        User user2 = new User();
        user2.setId(idGenerator.incrementAndGet());
        user2.setName("lisi");

        User user3 = new User();
        user3.setId(idGenerator.incrementAndGet());
        user3.setName("wangwu");

        User user4 = new User();
        user4.setId(idGenerator.incrementAndGet());
        user4.setName("zhaoliu");

        list.add(user);
        list.add(user2);
        list.add(user3);
        list.add(user4);
        return  list;
    }

    public User findById(int id){
        User user = new User();
        user.setId(id);
        user.setName("zhangsan");
        return  user;
    }

    /**
     * 保存用户对象
     * @param user
     * @return
     */
    public boolean save(User user){
        boolean success = false;
        //id从1开始
        Integer id = idGenerator.incrementAndGet();
        //把id放进去
        user.setId(id);
        return repository.put(id, user) == null;
    }
}
