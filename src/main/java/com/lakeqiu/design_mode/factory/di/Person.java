package com.lakeqiu.design_mode.factory.di;

/**
 * @author lakeqiu
 */
public class Person {
    private String name = "张三";
    private String age = "15";

    public Person() {
    }

    public Person(String name, String age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
