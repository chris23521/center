package com.ncarzone.data.simple.center.biz.enums;

/**
 * @author: caozhou
 * @create: 2020-11-06 15:00
 * @description:
 */
public enum JavaTypeEnum {
    STRING("String"),
    INTEGER("Integer"),
    INT( "int"),
    LONG( "Long"),
    DOUBLE( "Double"),
    DATE( "Date"),
    BOOLEAN("Boolean");


    String name;

    JavaTypeEnum(String name) {

        this.name = name;
    }

    public String getName() {
        return name;
    }
}
