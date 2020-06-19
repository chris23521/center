package com.ncarzone.data.simple.center.biz;

import com.ncarzone.data.simple.center.facade.SimpleServiceFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Copyright (c) 2018-2020 NCARZONE INFORMATION TECHNOLOGY CO.LTD.
 * All rights reserved.
 * This software is the confidential and proprietary information of NCARZONE
 * INFORMATION Technology CO.LTD("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with NCARZONE.
 * Created by chauncey on 20/6/18.
 */
public class SimpleServiceImpl implements SimpleServiceFacade {
    org.slf4j.Logger Logger = LoggerFactory.getLogger(SimpleServiceImpl.class);

    @Override
    public String sayHello(String username) throws Exception {
        Logger.info(username);
        return "Hello "+username;
    }
}
