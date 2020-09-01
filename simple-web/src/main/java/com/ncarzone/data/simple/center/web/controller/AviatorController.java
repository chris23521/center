package com.ncarzone.data.simple.center.web.controller;

import com.ncarzone.data.simple.center.facade.AviatorService;
import com.ncarzone.data.simple.center.facade.model.ResultSet;
import com.ncarzone.data.simple.center.model.ResultBean;
import com.ncarzone.data.simple.center.web.swagger.ApiScan;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;


@ApiScan
@Api(tags = { "aviator" })
@Controller
@RequestMapping("/aviator")
public class AviatorController {

    @Autowired
    private AviatorService aviatorService;


    @ApiOperation(value = "获取简单标签", notes = "获取简单标签", tags = { "aviator" })
    @RequestMapping(value = "/getUserTagInfo", method = { RequestMethod.POST})
    @ResponseBody
    public ResultSet<Map<String, String>> getUserTagInfo(String userId, String[] tagArr) throws Exception {
        return aviatorService.getUserTagInfo(userId, tagArr);
    }

    @ApiOperation(value = "获取复合标签", notes = "获取复合标签", tags = { "aviator" })
    @RequestMapping(value = "/getComplexTagInfo", method = { RequestMethod.POST})
    @ResponseBody
    public ResultSet<Map<String, String>> getComplexTagInfo(String userId, String[] tagArr) throws Exception {
        return aviatorService.getComplexTagInfo(userId, tagArr);
    }
}
