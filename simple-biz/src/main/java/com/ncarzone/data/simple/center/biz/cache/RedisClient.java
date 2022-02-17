package com.ncarzone.data.simple.center.biz.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

@Component
public class RedisClient {

    private static final Logger logger = LoggerFactory.getLogger(RedisClient.class);

    //@Resource(name = "jedisPool")
    //private JedisPool jedisPool;

    @Resource(name = "redisTemplate")
    private RedisTemplate redisTemplate;

    /**
     * 插入数据
     * @param key
     * @param value
     * @param expireTime
     */
    public void set(String key, String value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            logger.error("rpush redis插入异常 key={}, value={}, exception={}", key, value, e.getMessage(), e);
        }
    }

    //    删除多个key
    public void deleteKey (String ...keys){
        redisTemplate.delete(keys);
    }

    /**
     * 获取redis连接
     */
    //public Jedis getRessourceWithRetry() {
    //    try {
    //        return jedisPool.getResource();
    //    } catch (Exception e) {
    //        try {
    //            return jedisPool.getResource();
    //        } catch (Exception ex) {
    //            throw ex;
    //        }
    //    }
    //}

    /**
     * 追加 value 到列表中
     *
     * @param expireTime 生存时间  > 0时有效  0 表示生存时间为永久
     */
    //public void rpush(String key, String[] value, int expireTime) {
    //    if (!appConfig.getRedisUse()) {
    //        return ;
    //    }
    //    try {
    //        jedisCluster.rpush(key, value);
    //        if (expireTime > 0) {
    //            jedisCluster.expire(key, expireTime);
    //        }
    //    } catch (Exception e) {
    //        logger.error("rpush redis插入异常 key={}, value={}, exception={}", key, value, e.getMessage(), e);
    //    }
    //}

    //集群模式更改主从模式
    //public void rpush(String key, String[] value, int expireTime) {
    //    Jedis jedis = null;
    //    try {
    //        jedis = getRessourceWithRetry();
    //        jedis.rpush(key, value);
    //        if (expireTime > 0) {
    //            jedis.expire(key, expireTime);
    //        }
    //    } catch (Exception e) {
    //        logger.error("rpush redis插入异常 key={}, value={}, exception={}", key, value, e.getMessage(), e);
    //    } finally {
    //        close(jedis);
    //    }
    //
    //}

    /**
     * 取出redis中列表的数据
     *
     * @param start 0 开始
     * @param end   -1 结束
     */
    //public List<String> lrange(String key, long start, long end) {
    //    if (!appConfig.getRedisUse()) {
    //        return null;
    //    }
    //    try {
    //        return jedisCluster.lrange(key, start, end);
    //    } catch (Exception e) {
    //        logger.error("lrange redis查询异常 key={}, exception={}", key, e.getMessage(), e);
    //    }
    //    return null;
    //}

    /**
     * 集群模式切换主从模式
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    //public List<String> lrange(String key, long start, long end) {
    //
    //    Jedis jedis = null;
    //    try {
    //        jedis = getRessourceWithRetry();
    //        return jedis.lrange(key, start, end);
    //    } catch (Exception e) {
    //        logger.error("lrange redis查询异常 key={}, exception={}", key, e.getMessage(), e);
    //    } finally {
    //        close(jedis);
    //    }
    //    return null;
    //}

    //public Long llen(String key) throws Exception {
    //    if (!appConfig.getRedisUse()) {
    //        return null;
    //    }
    //    return jedisCluster.llen(key);
    //}
    //public Long llen(String key) throws Exception {
    //    Jedis jedis = null;
    //    try {
    //         jedis = getRessourceWithRetry();
    //        return jedis.llen(key);
    //    }catch (Exception e){
    //        logger.error("llen redis查询异常 key={}, exception={}", key, e.getMessage(), e);
    //    }finally {
    //        close(jedis);
    //    }
    //    return null;
    //}

    /**
     * 往redis中put字符串并设置有效期(单位/s)
     */
    //public void set(String key, final String value, int expireTime) {
    //    if (!appConfig.getRedisUse()) {
    //        return;
    //    }
    //    try {
    //        if (expireTime == -1) {
    //            jedisCluster.set(key, value);
    //        } else {
    //            jedisCluster.setex(key, expireTime, value);
    //        }
    //    } catch (Exception e) {
    //        logger.error("set redis缓存数据失败 key={}, exception={}", key, e.getMessage(), e);
    //    }
    //}
    //public void set(String key, final String value, int expireTime) {
    //    Jedis jedis = null;
    //    try {
    //        jedis = getRessourceWithRetry();
    //        if (expireTime == -1){
    //            jedis.set(key,value);
    //        }else {
    //            jedis.setex(key,expireTime,value);
    //        }
    //    }catch (Exception e){
    //        logger.error("set redis缓存数据失败 key={}, exception={}", key, e.getMessage(), e);
    //    }finally {
    //        close(jedis);
    //    }
    //}

    //public String get(String key) {
    //    if (!appConfig.getRedisUse()) {
    //        return null;
    //    }
    //    try {
    //        return jedisCluster.get(key);
    //    } catch (Exception e) {
    //        logger.error("get redis获取数据失败 key={}, exception={}", key, e.getMessage(), e);
    //    }
    //    return null;
    //}
    //public String get(String key) {
    //    Jedis jedis = null;
    //    try {
    //        jedis = getRessourceWithRetry();
    //        return jedis.get(key);
    //    }catch (Exception e){
    //        logger.error("get redis获取数据失败 key={}, exception={}", key, e.getMessage(), e);
    //    }finally {
    //        close(jedis);
    //    }
    //    return null;
    //}

    /**
     * 获取所有的key
     */
    //public List<String> getAllKey(){
    //    Jedis jedis = null;
    //    try {
    //        jedis = getRessourceWithRetry();
    //        Set<String> keys = jedis.keys("*");
    //        List<String> list = new ArrayList<>(keys);
    //        return list;
    //    }catch (Exception e){
    //        logger.error("get redis获取数据失败 exception={}", e.getMessage(), e);
    //    }finally {
    //        close(jedis);
    //    }
    //    return null;
    //}

    /**
     * 释放redis连接
     *
     * @param jedis
     */
    //public void close(Jedis jedis) {
    //    if (null != jedis) {
    //        jedis.close();
    //    }
    //}
}
