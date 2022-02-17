package com.ncarzone.data.simple.center.web.controller;

import java.util.List;

import com.ncarzone.data.simple.center.facade.RedisDataServiceFacade;
import com.ncarzone.data.simple.center.model.ResultBean;
import com.ncarzone.data.simple.center.web.swagger.ApiScan;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@ApiScan
@Api(tags = {"示例"})
@Controller
@RequestMapping("/redis")
public class RedisController {

    @Autowired
    private RedisDataServiceFacade redisDataServiceFacade;

    @ApiOperation(value = "通过key，查询value", notes = "通过key，查询value", tags = {"示例"})
    @RequestMapping(value = "/set", method = {RequestMethod.GET})
    @ResponseBody
    public ResultBean<Boolean> set(String key,String value) throws Exception {
        Boolean result = redisDataServiceFacade.insertValue(key, value);
        return ResultBean.getSuccessBean(result);
    }


    @ApiOperation(value = "通过key，查询value", notes = "通过key，查询value", tags = {"示例"})
    @RequestMapping(value = "/getValueByKey", method = {RequestMethod.GET})
    @ResponseBody
    public ResultBean<Object> url(String key) throws Exception {
        String result = redisDataServiceFacade.getValueByKey(key);
        System.out.println(result);
        return ResultBean.getSuccessBean(result);
    }

    @ApiOperation(value = "查询所有key", notes = "查询所有key", tags = {"示例"})
    @RequestMapping(value = "/getAllKeys", method = {RequestMethod.GET})
    @ResponseBody
    public List<String> getAllKeys() throws Exception {
        List<String> allKeys = redisDataServiceFacade.getAllKeys();
        System.out.println(allKeys);
        return allKeys;
    }
}
