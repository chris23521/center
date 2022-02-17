package com.ncarzone.data.simple.center.web.test;

public class Test {
    public static void main(String[] args) {
        person person = new person();
        person.setName("yang");
        if (person.getAge() == null|| person.getAge() < 0){
            System.out.println("log");
        }
    }
}
