package com.ncarzone.data.simple.center.biz.aviator;

import com.googlecode.aviator.AviatorEvaluator;
import com.googlecode.aviator.AviatorEvaluatorInstance;
import com.googlecode.aviator.Expression;
import com.googlecode.aviator.Options;
import com.ncarzone.data.simple.center.biz.util.JacksonUtil;
import com.ncarzone.data.simple.center.facade.model.ResultSet;
import com.ncarzone.data.simple.center.facade.model.ResultSetCode;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户人群标签表达式
 */
public class ComplexTagsExpression {


    private static final Logger logger = LoggerFactory.getLogger(ComplexTagsExpression.class);

    public static volatile Map<String, Expression> expressionMap = new ConcurrentHashMap();

    private static final Boolean TRUE = true;

    private static final String E = "E";

    private static final String DOT = ".";

    /**
     * 1:分类型标签
     */
    private static final char TAG_CLASSABLE_TYPE = '1';

    /**
     * 分类型标签值
     */
    private static final String TAG_CLASSABLE_VALUE = "1";

    /**
     * Aviator表达式引擎实例实例对象
     */
    private static AviatorEvaluatorInstance aviatorEvaluatorInstance =  null;

    /**
     * 初始化
     */
    public static void init() throws RuntimeException{
        if(aviatorEvaluatorInstance == null){
            aviatorEvaluatorInstance =  AviatorEvaluator.newInstance();
            aviatorEvaluatorInstance.setOption(Options.OPTIMIZE_LEVEL, AviatorEvaluator.EVAL);
        }

        /**
         * 人群id
         */
        String keyOne = "101201";
        String keyTwo = "101202";
        String keyThree = "101203";
        String keyFour = "101204";

        /**
         * 简单标签的组合
         * 用户最近登录距今天数 B220H082_0_005 = 24
         * 用户注册天数        A220H090_0_001 = 300
         */
        String ruleOne = "((B220H082_0_005>=24) && (A220H090_0_001>nil && A220H090_0_001<=400))";
        String ruleTwo = "((B220H082_0_005>=24) && (A220H090_0_001>nil && A220H090_0_001<=200))";
        String ruleThree = "((B220H082_0_005>=24) && !(A220H090_0_001>nil && A220H090_0_001<=200))";
        String ruleFour = "((B220H082_0_005>=24) || (A220H090_0_001>nil && A220H090_0_001<=200))";
        try {
            expressionMap.put(keyOne, aviatorEvaluatorInstance.compile(ruleOne));
            expressionMap.put(keyTwo, aviatorEvaluatorInstance.compile(ruleTwo));
            expressionMap.put(keyThree, aviatorEvaluatorInstance.compile(ruleThree));
            expressionMap.put(keyFour, aviatorEvaluatorInstance.compile(ruleFour));
        }catch (Exception e) {
            throw new RuntimeException("初始化编译人群规则异常", e);
        }
    }

    /**
     * 释放资源
     */
    public static void close(){
        if(expressionMap != null){
            expressionMap.clear();
            expressionMap = null;
        }

        if(aviatorEvaluatorInstance != null){
            aviatorEvaluatorInstance.clearExpressionCache();
            aviatorEvaluatorInstance = null;
        }
    }

    /**
     *
     * @param userId 用户编号
     * @param userTags 该用户下简单标签集合
     * @param complexTagArr 复合标签id列表
     * @return
     */
    public static ResultSet<Map<String, String>> getComplexTags(String userId, Map<String, String> userTags, String[] complexTagArr) {
        //检查必填参数
        ResultSet<Map<String, String>> resultSet = null;
        try {
            // 分类型标签和统计性标签规范化处理
            Map<String, Object> userTagMap = convertDataType(userId, userTags);

            Map<String, String> resultMap = execute(userTagMap, complexTagArr);

            resultSet = ResultSet.valueOf(ResultSetCode.SUCCESS, "success");

            resultSet.setResult(resultMap);

        }catch (Exception e) {
            logger.error("实时解析人群标签服务接口异常:", e);

        }
        return resultSet;
    }

    /**
     * 转换数据类型
     * 1、分类型标签值 转换为true
     * 2、统计型标签值 转换成数字类型（Double,Long）
     * 3、去掉E类型的标签，不需要
     * @param userTags
     * @return
     */
    private static Map<String, Object> convertDataType(String id, Map<String, String> userTags) throws Exception{

        Map<String, Object> objectMap = new HashMap<>();
        for (Map.Entry<String, String> entry : userTags.entrySet()) {
            String tagId = entry.getKey();
            if (tagId.startsWith(E)) {
                continue;
            }
            Object value;
            char tagType = tagId.charAt(1);
            // 分类型标签
            if (TAG_CLASSABLE_TYPE == tagType) {
                value = TRUE;
            }else {
                // 统计性标签
                String tagValue = entry.getValue();
                if(StringUtils.isEmpty(tagValue)){
                    // 此处极有可能因为标签value不规范导致过滤，为了以后便于排查问题，特记录error日志。
                    logger.error("统计性标签的值为空被过滤, id={}, tagId={}, 请检查", id, tagId);
                    continue;
                }
                if (tagValue.contains(DOT)) {
                    value = Double.valueOf(tagValue);
                }else {
                    value = Long.valueOf(tagValue);
                }
            }
            objectMap.put(tagId, value);
        }

        return objectMap;
    }

    /**
     * 获取用户所在的人群id
     * @param complexTagArr 限定人群范围列表
     * @param userTagMap 用户标签map <tagid, 数值>、<tagid, true/false>
     */
    private static Map<String, String> execute(Map<String, Object> userTagMap, String[] complexTagArr) throws Exception{
        List<String> complexTagIds = complexTagArr == null ? new ArrayList<>() : Arrays.asList(complexTagArr);

        Map<String, String> out = new HashMap<>();

        Map<String, Expression> expressionMap = ComplexTagsExpression.expressionMap;

        if (CollectionUtils.isEmpty(complexTagIds)) {
            for (Map.Entry<String, Expression> entry : expressionMap.entrySet()) {
                Expression expression = entry.getValue();

                Object obj = expression.execute(userTagMap);
                if (TRUE.equals(obj)) {
                    out.put(entry.getKey(), TAG_CLASSABLE_VALUE);
                }
            }
        } else {
            for (String complexTagId : complexTagIds) {
                Expression expression = expressionMap.get(complexTagId);

                Object obj = expression.execute(userTagMap);
                if (TRUE.equals(obj)) {
                    out.put(complexTagId, TAG_CLASSABLE_VALUE);
                }
            }
        }

        return out;
    }
}
