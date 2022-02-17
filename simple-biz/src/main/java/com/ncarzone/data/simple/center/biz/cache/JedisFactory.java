package com.ncarzone.data.simple.center.biz.cache;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPool;

public class JedisFactory implements FactoryBean<JedisPool> , InitializingBean {

    private Logger logger = LoggerFactory.getLogger(JedisFactory.class);

    private JedisPool jedisPool;
    private String hosts;
    private int connectionTimeout;
    private int soTimeout;
    private String password;
    private int database;
    private GenericObjectPoolConfig genericObjectPoolConfig;

    @Override
    public JedisPool getObject() throws Exception {
        return jedisPool;
    }

    @Override
    public Class<? extends JedisPool> getObjectType() {
        return (this.jedisPool != null ? this.jedisPool.getClass() : JedisPool.class);
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    @Override
    public void afterPropertiesSet() {
        try {
            if (StringUtils.isEmpty(hosts)) {
                logger.error("|JedisFactory|afterPropertiesSet|hosts is null");
                return;
            }
            String[] hostArr = hosts.split(",");
            String[] ipInfoArr = hostArr[0].split(":");
            String host = ipInfoArr[0];
            int port;
            if (ipInfoArr.length > 1) {
                port = Integer.valueOf(ipInfoArr[1]);
            } else {
                port = 6379;
            }

            jedisPool = new JedisPool(genericObjectPoolConfig, host, port, connectionTimeout, soTimeout, password, database, null);
        } catch (Exception e) {
            logger.error("jedisPool init failed:{}", e.getMessage(), e);
        }
    }

    public void setHosts(String hosts) {
        this.hosts = hosts;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public void setSoTimeout(int soTimeout) {
        this.soTimeout = soTimeout;
    }

    public void setGenericObjectPoolConfig(GenericObjectPoolConfig genericObjectPoolConfig) {
        this.genericObjectPoolConfig = genericObjectPoolConfig;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDatabase(int database) {
        this.database = database;
    }
}
