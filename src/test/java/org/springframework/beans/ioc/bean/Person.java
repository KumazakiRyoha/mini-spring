package org.springframework.beans.ioc.bean;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * 用于测试的Person类
 */
public class Person implements InitializingBean, DisposableBean {
    private String name;
    private int age;
    private Car car; // 确保该字段存在

    public Person() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", car=" + car +
                '}';
    }

    public void customInitMethod() {
        System.out.println("Person custom init method called");
    }

    public void customDestroyMethod() {
        System.out.println("Person custom destroy method called");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("Person afterPropertiesSet called");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("Person destroy method called");
    }

}
