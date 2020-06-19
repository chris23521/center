package com.ncarzone.data.simple.center.web.controller;

import com.ncarzone.data.simple.center.facade.SimpleServiceFacade;
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
@Api(tags = { "测试Quick BI URL" })
@Controller
@RequestMapping("/simple")
public class SimpleController {

	@Autowired
	private SimpleServiceFacade simpleServiceFacade;

	@ApiOperation(value = "示例url", notes = "示例url", tags = { "示例" })
	@RequestMapping(value = "/url", method = { RequestMethod.GET })
	@ResponseBody
	public ResultBean<Object> url(String username) throws Exception {
		String  result = simpleServiceFacade.sayHello(username);
		return ResultBean.getSuccessBean(result);
	}

}
