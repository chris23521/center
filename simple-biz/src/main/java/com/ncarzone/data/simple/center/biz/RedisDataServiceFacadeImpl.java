package com.ncarzone.data.simple.center.biz;

import java.util.List;

import com.ncarzone.data.simple.center.biz.cache.RedisClient;
import com.ncarzone.data.simple.center.facade.RedisDataServiceFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("redisDataServiceFacade")
public class RedisDataServiceFacadeImpl implements RedisDataServiceFacade {

    @Autowired
    private RedisClient redisClient;

    @Override
    public String getValueByKey(String key) {

        //String s = redisClient.get(key);
        //return s;
        return null;
    }

    @Override
    public List<String> getAllKeys() {
        //List<String> allKey = redisClient.getAllKey();
        //return allKey;
        return null;
    }



    @Override
    public Boolean insertValue(String key, String value) {

        redisClient.set(key,value);
        return true;
    }
}
