
package com.ncarzone.data.simple.center.biz;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.base.Functions;
import com.google.common.collect.Maps;
import com.ncarzone.data.simple.center.biz.aviator.ComplexTagsExpression;
import com.ncarzone.data.simple.center.biz.util.JacksonUtil;
import com.ncarzone.data.simple.center.facade.AviatorService;
import com.ncarzone.data.simple.center.facade.model.ResultSet;
import com.ncarzone.data.simple.center.facade.model.ResultSetCode;
import org.springframework.util.CollectionUtils;
import java.util.*;

public class AviatorServiceImpl implements AviatorService {

    /**
     * Description: 获取用户标签接口
     * @param userId 用户标识
     * @param userTagArr 用户标签数组，空数组，则取该用户所有标签，非空则取tagArr范围内的标签
     * @return
     */
    @Override
    public  ResultSet<Map<String, String>>  getUserTagInfo(String userId, String[] userTagArr){
        String json = "{\"B220H082_0_009\":\"7.0\",\"B220H082_0_008\":\"9.0\",\"B220H082_0_006\":\"10.0\",\"C120H025_7_002\":\"2\",\"B220H082_0_001\":\"1.0\",\"A120H189_0_007\":\"\",\"B220H082_0_005\":\"24.0\",\"B220H022_0_001\":\"5.0\",\"B220H082_0_004\":\"6.0\",\"B220H082_0_002\":\"106.0\",\"C120H009_7_003\":\"3\",\"B220H015_0_001\":\"16.0\",\"A120H090_0_001\":\"\",\"B220H081_0_004\":\"1.0\",\"A111H001_0_001\":\"130.0\",\"B220H081_0_006\":\"9.0\",\"B220H081_0_005\":\"3.0\",\"B220H081_0_002\":\"14.0\",\"B220H081_0_001\":\"1.0\",\"B220H111_0_002\":\"20190510\",\"B220H111_0_001\":\"20190514\",\"E220H088_0_001\":\"\",\"B220H111_0_004\":\"20190505\",\"B220H111_0_003\":\"20190218\",\"B220H111_0_005\":\"20181124\",\"C120H025_6_002\":\"1\",\"C120H010_7_003\":\"1\",\"B220H039_0_001\":\"10\",\"B220H081_0_014\":\"1.0\",\"A220H029_0_001\":\"304\",\"B220H081_0_011\":\"19.0\",\"B220H081_0_013\":\"2.0\",\"C120H017_6_003\":\"1\",\"C120H017_6_009\":\"1\",\"A121H031_0_002\":\"-999999\",\"A121H101_0_020\":\"\",\"A121H002_0_011\":\"\",\"A120H100_0_002\":\"\",\"A221H024_0_001\":\"\",\"C120H017_7_003\":\"1\",\"B220H080_0_006\":\"5.0\",\"C120H017_7_009\":\"1\",\"A120H089_0_011\":\"\",\"C120H017_6_011\":\"2\",\"B220H038_0_001\":\"172\",\"B220H082_0_012\":\"1.0\",\"B220H082_0_011\":\"82.0\",\"B220H082_0_010\":\"1.0\",\"B220H066_0_001\":\"2\",\"B220H082_0_015\":\"2.0\",\"B220H082_0_014\":\"6.0\",\"B220H082_0_013\":\"5.0\",\"C120H025_3_002\":\"1\",\"C120H017_7_011\":\"2\",\"A121H030_0_002\":\"309\",\"E220H089_0_001\":\"1080x1920\",\"A111H085_0_001\":\"0.8498029112897362\",\"B220H034_0_001\":\"0.0\",\"B220H034_0_002\":\"0.0\",\"E220H091_0_001\":\"en\",\"C120H018_7_005\":\"2\",\"C120U001_6_027\":\"2\",\"A121H002_0_010\":\"\",\"A220H090_0_001\":\"300\",\"A221H024_0_004\":\"\",\"C120U001_6_002\":\"2\",\"A121U002_0_002\":\"-999999\"}";

        JacksonUtil.deserializeListIgnoreException(json, new TypeReference<List<String>>() {});
        JSONObject obj = JSON.parseObject(json);
        Map<String, String> data = Maps.transformValues(obj, Functions.toStringFunction());
        ResultSet result = ResultSet.valueOf(ResultSetCode.SUCCESS);
        result.setResult(data);
        return result;
    }

    /**
     * 获取组合标签
     * @param userId 用户标识
     * @param complexTagArr 组合标签数组，空数组，则取该用户所有标签，非空则取tagArr范围内的标签
     * @return
     */
    @Override
    public ResultSet<Map<String, String>> getComplexTagInfo(String userId, String[] complexTagArr){

        ResultSet<Map<String, String>> resultSet = getUserTagInfo(userId,null);
        Map<String, String> userTags = resultSet.getResult();

        if(CollectionUtils.isEmpty(userTags)){
            return resultSet;
        }

        return ComplexTagsExpression.getComplexTags(userId, userTags, complexTagArr);

    }
}
