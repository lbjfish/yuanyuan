package com.lee.testspringboot2.po;

import java.util.function.Supplier;

public class User {
    private int id;
    private String name="dfdfs";

    public User() {
    }

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public static void main(String[] args) {
        ConcreteSubject concreteSubject = new ConcreteSubject();

        concreteSubject.attach(new ConcreteObserver(concreteSubject, "X"));
        concreteSubject.attach(new ConcreteObserver(concreteSubject, "Y"));
        concreteSubject.attach(new ConcreteObserver(concreteSubject, "Z"));

        concreteSubject.setSubjectState("ABC");
        concreteSubject.notifyObserver();
    }
}
