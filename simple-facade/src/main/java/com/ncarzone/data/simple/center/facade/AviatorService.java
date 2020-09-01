/**
 * Copyright       (C), 2017-2018, 浙江执御信息技术有限公司
 * FileName:       TagRiskService
 * Author:         Aubrey
 * Date:           2018/6/8 16:14
 * Description:    离线用户标签
 */
package com.ncarzone.data.simple.center.facade;


import com.ncarzone.data.simple.center.facade.model.ResultSet;

import java.util.Map;

public interface AviatorService {
    /**
     * 获取用户标签接口
     * @param userId 用户标识
     * @param userTagArr 用户标签数组，空数组，则取该用户所有标签，非空则取tagArr范围内的标签
     */
    public ResultSet<Map<String, String>> getUserTagInfo(String userId, String[] userTagArr);

    /**
     * 获取组合标签
     * @param userId 用户标识
     * @param complexTagArr 组合标签数组，空数组，则取该用户所有标签，非空则取tagArr范围内的标签
     */
    public ResultSet<Map<String, String>> getComplexTagInfo(String userId, String[] complexTagArr);

}
