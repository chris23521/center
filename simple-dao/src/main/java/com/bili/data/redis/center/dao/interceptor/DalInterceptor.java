package com.bili.data.redis.center.dao.interceptor;

import java.util.Properties;

import com.alibaba.fastjson.JSONObject;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: 苏释
 * @create: 2020年08月26日 17:31
 * @description:
 */
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
    @Signature(type = Executor.class, method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class DalInterceptor implements Interceptor {
    private Logger LOGGER = LoggerFactory.getLogger("PROJECT-DAL");

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        long startTime = System.currentTimeMillis();
        MappedStatement mappedStatement = (MappedStatement)invocation.getArgs()[0];
        if (LOGGER.isDebugEnabled()) {
            Object param = invocation.getArgs()[1];
            LOGGER.debug(JSONObject.toJSONString(param));
        }
        try {
            return invocation.proceed();
        } finally {
            final StringBuilder build = new StringBuilder(7);
            build.append(mappedStatement.getId());
            build.append("|");
            build.append((System.currentTimeMillis() - startTime));
            build.append("ms");
            LOGGER.info(build.toString());
        }
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {

    }
}
