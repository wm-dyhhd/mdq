package com.dyhhd.mdq.simple;

/**
 * 我的任务
 *
 * @author lv ning
 */
public class MyWorker {

    private int age;

    private String name;

    public MyWorker(int age, String name) {
        this.age = age;
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "MyWorker{" +
                "age=" + age +
                ", name='" + name + '\'' +
                '}';
    }
}
