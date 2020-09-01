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
    private static final String TAG_CLASSABLE_VALUE = "-999999";

    /**
     * Aviator表达式引擎实例实例对象
     */
    private static AviatorEvaluatorInstance aviatorEvaluatorInstance =  null;


    /**
     * 全量加载人群数据到缓存
     * @return
     */
    public static void init() throws RuntimeException{
        //初始化Aviator表达式引擎实例对象
        if(aviatorEvaluatorInstance == null){
            aviatorEvaluatorInstance =  AviatorEvaluator.newInstance();
            //Optimized for execute speed,this is the default option:以运行时的性能优先
            aviatorEvaluatorInstance.setOption(Options.OPTIMIZE_LEVEL, AviatorEvaluator.EVAL);
        }

        /*ConfigChangeListener changeListener = new ConfigChangeListener() {
            @Override
            @ApolloConfigChangeListener("usercategory_app")
            public void onChange(ConfigChangeEvent configChangeEvent) {
                logger.info("Changes for namespace {}", configChangeEvent.getNamespace());
                try{
                    for (String key : configChangeEvent.changedKeys()) {
                        ConfigChange change = configChangeEvent.getChange(key);
                        logger.info("Apollo Change - key: {}, oldValue: {}, newValue: {}, changeType: {}",
                                change.getPropertyName(), change.getOldValue(), change.getNewValue(),
                                change.getChangeType());
                        String newValue = change.getNewValue();

                        //监听御策人群规则发生变化（增删改）
                        if (StringUtils.isEmpty(newValue)) {
                            expressionMap.remove(key);
                        }else {
                            String rule = AnalyzeRuleUtil.analyze(key, newValue);
                            Expression expression = getExpression(rule);
                            expressionMap.put(key, expression);
                        }
                    }
                }catch (Exception e){
                    logger.error("ConfigChangeListener onchange fail:",e);
                }
            }
        };*/

        /**
         * 人群id
         */
        String key = "101201";
        /**
         * 简单标签的组合
         */
        String value = null;
        try {
            /**
             *
             */
            String rule = AnalyzeRuleUtil.analyze(key, value);

            expressionMap.put(key, aviatorEvaluatorInstance.compile(rule));
        }catch (Exception e) {
            logger.error("初始化编译人群[" + key + "]规则异常", e);
            throw new RuntimeException("初始化编译人群[" + key + "]规则异常", e);
        }

        logger.info("初始化编译人群成功,人群ids：" + expressionMap.keySet());
    }

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
            long start = System.currentTimeMillis();

            // 分类型标签和统计性标签规范化处理
            Map<String, Object> userTagMap = convertDataType(userId, userTags);

            Map<String, String> resultMap = getgetComplexTagIdList(userId, userTagMap, complexTagArr);

            resultSet = ResultSet.valueOf(ResultSetCode.SUCCESS, "success");
            resultSet.setResult(resultMap);

            long end = System.currentTimeMillis();

            if((end-start) > 10){
                logger.info(userId+" 实时解析人群标签总耗时:"+(end-start)+"ms,userTagData:"+ JacksonUtil.serialize(userTags));
            }else {
                logger.info(userId+" 实时解析人群标签总耗时:"+(end-start)+"ms");
            }
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
        long start = System.currentTimeMillis();

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

        long end = System.currentTimeMillis();

        //超过10毫秒打印具体userTagMap信息
        if((end-start) > 10){
            logger.info(id+" convertDataType cost:"+(end-start)+"ms"+",userTags:"+JacksonUtil.serialize(userTags));
        }else{
            logger.info(id+" convertDataType cost:"+(end-start)+"ms");
        }

        return objectMap;
    }

    /**
     * 获取用户所在的人群id
     * @param complexTagArr 限定人群范围列表
     * @param userTagMap 用户标签map <tagid, 数值>、<tagid, true/false>
     * @return map<人群id, -999999>
     */
    private static Map<String, String> getgetComplexTagIdList(String userId, Map<String, Object> userTagMap, String[] complexTagArr) throws Exception{
        List<String> complexTagIds = complexTagArr == null ? new ArrayList<>() : Arrays.asList(complexTagArr);

        Map<String, String> out = new HashMap<>();

        // key为complexTagId，value为aviator表达式对象
        Map<String, Expression> expressionMap = ComplexTagsExpression.expressionMap;

        if (CollectionUtils.isEmpty(complexTagIds)) {
            for (Map.Entry<String, Expression> entry : expressionMap.entrySet()) {
                execute(userId, entry.getKey(), entry.getValue(), userTagMap, out);
            }
        } else {
            for (String complexTagId : complexTagIds) {
                Expression expression = expressionMap.get(complexTagId);
                if (expression == null) {
                    continue;
                }
                // cookieId, 人群id 200102，avaitor表达式，人的简单标签map，出参
                execute(userId, complexTagId, expression, userTagMap, out);
            }
        }

        return out;
    }

    /**
     * 解析
     * @param userId 用户id
     * @param complexTagId 人群id
     * @param expression 解析对象
     * @param userTagMap
     * @param resultMap 结果集
     * @throws Exception
     */
    private static void execute(String userId, String complexTagId, Expression expression, Map<String, Object> userTagMap, Map<String, String> resultMap)
            throws Exception{
        long start = System.currentTimeMillis();

        Object obj = expression.execute(userTagMap);

        long end = System.currentTimeMillis();

        if((end-start) > 1){
            logger.info(userId+" 实时解析人群id:"+complexTagId+" cost:"+(end-start)+"ms");
        }

        if (TRUE.equals(obj)) {
            resultMap.put(complexTagId, TAG_CLASSABLE_VALUE);
        }
    }

    public static void main(String...args){
        try{
            System.setProperty("apollo.cacheDir","/Users/kim/dev/apollo");
            System.setProperty("app.id","service_center");
            System.setProperty("apollo.meta","http://172.31.2.218:30002/");

            init();
            System.out.println(expressionMap.size());

            for(Map.Entry<String, Expression> entry : expressionMap.entrySet()){
                System.out.println(entry.getKey()+": "+entry.getValue().getVariableFullNames());
            }

            String tags = "{\"B220H082_0_009\":\"7.0\",\"B220H082_0_008\":\"9.0\",\"B220H082_0_006\":\"10.0\",\"C120H025_7_002\":\"2\",\"B220H082_0_001\":\"1.0\",\"A120H189_0_007\":\"\",\"B220H082_0_005\":\"24.0\",\"B220H022_0_001\":\"5.0\",\"B220H082_0_004\":\"6.0\",\"B220H082_0_002\":\"106.0\",\"C120H009_7_003\":\"3\",\"B220H015_0_001\":\"16.0\",\"A120H090_0_001\":\"\",\"B220H081_0_004\":\"1.0\",\"A111H001_0_001\":\"130.0\",\"B220H081_0_006\":\"9.0\",\"B220H081_0_005\":\"3.0\",\"B220H081_0_002\":\"14.0\",\"B220H081_0_001\":\"1.0\",\"B220H111_0_002\":\"20190510\",\"B220H111_0_001\":\"20190514\",\"E220H088_0_001\":\"\",\"B220H111_0_004\":\"20190505\",\"B220H111_0_003\":\"20190218\",\"B220H111_0_005\":\"20181124\",\"C120H025_6_002\":\"1\",\"C120H010_7_003\":\"1\",\"B220H039_0_001\":\"10\",\"B220H081_0_014\":\"1.0\",\"A220H029_0_001\":\"304\",\"B220H081_0_011\":\"19.0\",\"B220H081_0_013\":\"2.0\",\"C120H017_6_003\":\"1\",\"C120H017_6_009\":\"1\",\"A121H031_0_002\":\"-999999\",\"A121H101_0_020\":\"\",\"A121H002_0_011\":\"\",\"A120H100_0_002\":\"\",\"A221H024_0_001\":\"\",\"C120H017_7_003\":\"1\",\"B220H080_0_006\":\"5.0\",\"C120H017_7_009\":\"1\",\"A120H089_0_011\":\"\",\"C120H017_6_011\":\"2\",\"B220H038_0_001\":\"172\",\"B220H082_0_012\":\"1.0\",\"B220H082_0_011\":\"82.0\",\"B220H082_0_010\":\"1.0\",\"B220H066_0_001\":\"2\",\"B220H082_0_015\":\"2.0\",\"B220H082_0_014\":\"6.0\",\"B220H082_0_013\":\"5.0\",\"C120H025_3_002\":\"1\",\"C120H017_7_011\":\"2\",\"A121H030_0_002\":\"309\",\"E220H089_0_001\":\"1080x1920\",\"A111H085_0_001\":\"0.8498029112897362\",\"B220H034_0_001\":\"0.0\",\"B220H034_0_002\":\"0.0\",\"E220H091_0_001\":\"en\",\"C120H018_7_005\":\"2\",\"C120U001_6_027\":\"2\",\"A121H002_0_010\":\"\",\"A220H090_0_001\":\"300\",\"A221H024_0_004\":\"\",\"C120U001_6_002\":\"2\",\"A121U002_0_002\":\"-999999\"}";

            Map<String, String> stringMap = JacksonUtil.deserialize(tags,Map.class);
            long start = System.currentTimeMillis();
            ResultSet<Map<String, String>> resultSet = getComplexTags("test",stringMap,null);
            System.out.println("getUserCategoryIdList cost:"+(System.currentTimeMillis()-start));

            /*start = System.currentTimeMillis();
            resultMap = getCategoryList(null,objectMap);
            System.out.println("getCategoryList cost two:"+(System.currentTimeMillis()-start));*/

            System.out.println("resultMap:"+JacksonUtil.serialize(resultSet.getResult())+",size:"+resultSet.getResult().size());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
