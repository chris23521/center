package com.ncarzone.data.simple.center.facade;

import java.util.List;

public interface RedisDataServiceFacade {


    /**
     * 根据key查找value
     */
    String getValueByKey(String key);

    /**
     * 获取所有的key值
     */
    List<String> getAllKeys();

    /**
     * 获取key-value落库mysql
     */
    Boolean insertValue(String key,String value);
}
