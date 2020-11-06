package com.ncarzone.data.simple.center.biz.enums;

/**
 * @author: caozhou
 * @create: 2020-11-06 15:00
 * @description:
 */
public enum DbTypeEnum {
    VARCHAR("varchar(128)"),
    DECIMAL("decimal(12, 2)"),
    INT( "int(11)"),
    BIGINT( "bigint(20)"),
    DATETIME( "datetime"),
    TINYINT("tinyint(1)");


    String name;

    DbTypeEnum(String name) {

        this.name = name;
    }

    public String getName() {
        return name;
    }
}
