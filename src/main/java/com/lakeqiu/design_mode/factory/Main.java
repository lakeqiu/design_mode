package com.lakeqiu.design_mode.factory;

import com.lakeqiu.design_mode.factory.di.ClassXmlApplicationContext;

/**
 * @author lakeqiu
 */
public class Main {
    public static void main(String[] args) {
        ClassXmlApplicationContext xmlApplicationContext = new ClassXmlApplicationContext("beans.xml");
        Object person = xmlApplicationContext.getBean("person");
        System.out.println(person.toString());
    }
}
