package com.ncarzone.data.simple.center.biz.aviator;


import com.ncarzone.data.simple.center.biz.util.JacksonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



public class AnalyzeRuleUtil {

    private static final Logger logger = LoggerFactory.getLogger(AnalyzeRuleUtil.class);

    /**
     * 括号常量
     */
    private static final char LEFT_PARENTHESIS='(',  RIGHT_PARENTHESIS=')', LEFT_BRACKET='[',  RIGHT_BRACKET=']';

    /**
     * 空字符串
     */
    private static final String EMPTY_STR = "";

    /**
     * 标点符号常量
     */
    private static final String COMMA = ",";

    /**
     * null字符串
     */
    private static final String NULL = "null", FALSE = "false", NIL = "nil";

    /**
     * 运算符常量
     */
    private static final String OPERATOR1=">=", OPERATOR2=">", OPERATOR3="<=", OPERATOR4="<",  OPERATOR5="==", AND=" && "
            , OR=" || ", NO="!", TERNARY="==nil ? false : true";

    /**
     * 带减法的规则解析
     * @param rule
     * @return
     * @throws Exception
     */
    public static String analyze(String id, String rule) throws RuntimeException{
        String result = null;
        try {
            String[] ruleArray = rule.split(NO);
            List<String> resultRuleList = new ArrayList<>();
            for (String sub : ruleArray) {
                if (StringUtils.isEmpty(sub) || sub.equals("[]")){
                    throw new RuntimeException("人群规则不能为空");
                }
                String subRule = analyzeRule(sub);
                subRule = LEFT_PARENTHESIS + subRule + RIGHT_PARENTHESIS;
                resultRuleList.add(subRule);
            }
            result = String.join(AND  + NO, resultRuleList);
        }catch (Exception e) {
            throw new RuntimeException("解析人群规则异常" , e);
        }

        logger.info("人群id:" + id + " ,转换前rule:"+rule+",\n转换后rule:"+result);
        return result;
    }

    /**
     * c类标签与c类标签之间是 ||关系，所有c类标签与其它大块非c类标签是 && 关系，大块非c类之间是 &&的关系，小块非c类之间是 || 关系
     */
    public static void main(String[] args) {
        String ruleBefore = "[{\"B220H066_0_001\":\"[1,null]\"},{\"A220H090_0_001\":\"[null,200]\"},{\"A221H024_0_004\":\"\"},{\"C221H024_0_004\":\"[1, 5]\"}]![{\"A111H001_0_001\":\"[100,150]\"},{\"B220H111_0_002\":\"[20190510,20190510]\"}]";
        /**
         * ((B220H066_0_001>=1) && (A220H090_0_001>nil && A220H090_0_001<=200) && (A221H024_0_004==nil ? false : true) && ((C221H024_0_004>=1 && C221H024_0_004<=5))) && !((A111H001_0_001>=100 && A111H001_0_001<=150) && (B220H111_0_002>=20190510 && B220H111_0_002<=20190510))
         */
        String ruleAfter = analyze("1001", ruleBefore);
        System.out.println(ruleAfter);
    }

    /**
     * 解析一个规则
     * @param rule
     * @return
     * @throws Exception
     *
     * 人群规则默认前端按照a、b、c类排序过所以从数据库取出来的已经是此顺序，
     * c类标签与c类标签之间是 ||关系，所有c类标签与其它大块非c类标签是 && 关系，大块非c类之间是 &&的关系，小块非c类之间是 || 关系
     */
    private static String analyzeRule(String rule) throws Exception {
        List<String> list = new ArrayList<>();
        List<Map<String, String>> tagList = JacksonUtil.deserialize(rule, List.class);
        StringBuilder cExp = new StringBuilder();

        //c类标签开始标识
        boolean isCtag = false;

        for (Map<String, String> map : tagList) {
            if (map.keySet().iterator().next().startsWith("C")) {
                if(!isCtag){
                    cExp.append(LEFT_PARENTHESIS);
                    isCtag = true;
                }
                handleCTag(map, cExp);
                continue;
            }
            String normalTag = handleNormalTag(map);
            list.add(normalTag);
        }

        //这里其实只有人群规则包含c标签最后一个才有可能为OR
        if (cExp.toString().endsWith(OR)) {
            cExp.delete(cExp.lastIndexOf(OR), cExp.length());

            if(isCtag){
                cExp.append(RIGHT_PARENTHESIS);
            }

            list.add(cExp.toString());
        }
        return String.join(AND, list);
    }

    /**
     * 解析普通标签
     * @param map
     * @return
     */
    private static String handleNormalTag(Map<String, String> map) throws Exception{
        StringBuilder exp = new StringBuilder();
        if (map.size() > 1) {
            exp.append(LEFT_PARENTHESIS);
        }
        String value = map.values().iterator().next();
        if (StringUtils.isEmpty(value)) {
            for (String key : map.keySet()) {
                exp.append(LEFT_PARENTHESIS);
                exp/*.append(key).append(OPERATOR5).append(FALSE).append(OR)*/.append(key).append(TERNARY);
                exp.append(RIGHT_PARENTHESIS);
                exp.append(OR);
            }
        }else {
            handleStatisticTag(exp, map.entrySet().iterator().next());
            exp.append(OR);
        }
        exp.delete(exp.lastIndexOf(OR), exp.length());
        if (map.size() > 1) {
            exp.append(RIGHT_PARENTHESIS);
        }
        return exp.toString();
    }

    /**
     * 解析C类标签
     * @param map
     * @param cExp
     */
    private static void handleCTag(Map<String, String> map,  StringBuilder cExp) throws Exception{
        if (map.size() > 1) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                cExp.append(LEFT_PARENTHESIS);
                cExp/*.append(entry.getKey()).append(OPERATOR5).append(FALSE).append(OR)*/.append(entry.getKey()).append(TERNARY);
                cExp.append(RIGHT_PARENTHESIS);
                cExp.append(OR);
            }
        }else {
            handleStatisticTag(cExp, map.entrySet().iterator().next());
            cExp.append(OR);
        }
    }


    /**
     * 将[1, 5] 或者 (1, 5)转换成(tagkey>=1 && tagkey<=5) 或者(tagkey>1 && tagkey<5)
     * @param cSqlCondition
     * @param tagEntry
     * @return
     */
    private static void handleStatisticTag(StringBuilder cSqlCondition, Map.Entry<String, String> tagEntry) throws Exception{
        String leftOperator = EMPTY_STR, rightOperator = EMPTY_STR;
        char left = LEFT_PARENTHESIS, right = RIGHT_PARENTHESIS;
        String tagValue = tagEntry.getValue();
        if (tagValue.indexOf(LEFT_PARENTHESIS) != -1) {
            leftOperator = OPERATOR2;
            left = LEFT_PARENTHESIS;
        }else if (tagValue.indexOf(LEFT_BRACKET) != -1) {
            leftOperator = OPERATOR1;
            left = LEFT_BRACKET;
        }
        if (tagValue.indexOf(RIGHT_PARENTHESIS) != -1) {
            rightOperator = OPERATOR4;
            right = RIGHT_PARENTHESIS;
        }else if (tagValue.indexOf(RIGHT_BRACKET) != -1) {
            rightOperator = OPERATOR3;
            right = RIGHT_BRACKET;
        }

        String subTagValue = tagValue.substring(tagValue.indexOf(left) + 1, tagValue.indexOf(right));
        String[] array = subTagValue.split(COMMA);
        for (int i=0; i<array.length; i++) {
            array[i] = array[i].trim();
        }
        cSqlCondition.append(LEFT_PARENTHESIS);
        if (StringUtils.isNotEmpty(array[0])) {
            /**
             * 对于{"A220H090_0_001":"[null,6]"} 这样的标签要识别成(A220H090_0_001 > nil && A220H090_0_001 <= 6) 而不能识别成(A220H090_0_001 <= 6),否则解析的时候会有误
             * 示例如下：
             * 1.有一个cookieId = ff1d4c65-0a4c-4b9c-9ee6-5c5d5a3a1d5d，此cookie只含有一个简单标签 tagMap = {"A121H031_0_002":"-999999"}
             * 2.现有一个人群 groupId = 331077，其规则为[{"A121H031_0_002":""},{"A220H090_0_001":"[null,6]"}]
             * 3. groupId = 331077的规则经过aviator编译后生产的规则表达式为 rule = ((A121H031_0_002==nil ? false : true) && (A220H090_0_001 <= 6))
             * 4. 那么,上述规则表达式经过expression.execute(tagMap)解析后，结果为true (应该为false，因为没有标签 A121H031_0_002，这样就和张勇的计算逻辑相同了)
             * 修改方式：
             * 第三步： groupId = 331077的规则经过aviator编译后生产的 rule = ((A121H031_0_002==nil ? false : true) && (A220H090_0_001 > nil && A220H090_0_001 <= 6))
             */
            if(NULL.equals(array[0].toLowerCase())) {
                cSqlCondition.append(tagEntry.getKey()).append(OPERATOR2).append(NIL);
            } else {
                cSqlCondition.append(tagEntry.getKey()).append(leftOperator).append(array[0]);
            }

            cSqlCondition.append(AND);
        }
        if (StringUtils.isNotEmpty(array[1])&&!NULL.equals(array[1].toLowerCase())) {
            cSqlCondition.append(tagEntry.getKey()).append(rightOperator).append(array[1]);
        }
        if (cSqlCondition.toString().endsWith(AND)) {
            cSqlCondition.delete(cSqlCondition.lastIndexOf(AND), cSqlCondition.length());
        }
        cSqlCondition.append(RIGHT_PARENTHESIS);
    }

    /*public static void main(String...args){
        try{
            String groupVaule = "[{\"A121U002_0_002\":\"\",\"A121U002_0_010\":\"\",\"A121U002_0_001\":\"\"},{\"C120U001_6_001\":\"[1,null]\"},{\"C120U001_6_002\":\"[1,null]\"},{\"C120U001_6_005\":\"[1,null]\"},{\"C120U001_6_006\":\"[1,null]\"},{\"C120U001_6_008\":\"[1,null]\"},{\"C120U001_6_009\":\"[1,null]\"},{\"C120U001_6_013\":\"[1,null]\"},{\"C120U001_6_027\":\"[1,null]\"},{\"C120U001_6_028\":\"[1,null]\"},{\"C120U001_6_029\":\"[1,null]\"},{\"C120U001_6_032\":\"[1,null]\"},{\"C120U001_6_033\":\"[1,null]\"},{\"C120U001_6_035\":\"[1,null]\"},{\"C120U001_6_037\":\"[1,null]\"},{\"C120U036_6_001\":\"[1,null]\"},{\"C120U036_6_002\":\"[1,null]\"},{\"C120U036_6_003\":\"[1,null]\"},{\"C120U036_6_004\":\"[1,null]\"},{\"C120U036_6_005\":\"[1,null]\"},{\"C120U036_6_006\":\"[1,null]\"},{\"C120U036_6_007\":\"[1,null]\"},{\"C120U036_6_009\":\"[1,null]\"},{\"C120U036_6_010\":\"[1,null]\"},{\"C120U036_6_011\":\"[1,null]\"},{\"C120U036_6_012\":\"[1,null]\"},{\"C120U036_6_013\":\"[1,null]\"},{\"C120U036_6_015\":\"[1,null]\"},{\"C120U003_6_001\":\"[1,null]\"},{\"C120U003_6_002\":\"[1,null]\"},{\"C120U003_6_003\":\"[1,null]\"},{\"C120U003_6_004\":\"[1,null]\"},{\"C120U003_6_005\":\"[1,null]\"},{\"C120U003_6_007\":\"[1,null]\"},{\"C120U003_6_008\":\"[1,null]\"},{\"C120U003_6_009\":\"[1,null]\"},{\"C120U003_6_010\":\"[1,null]\"},{\"C120U003_6_012\":\"[1,null]\"}]![{\"A121U002_0_010\":\"\",\"A121U002_0_011\":\"\",\"A121U002_0_003\":\"\"},{\"A111U041_0_002\":\"\"},{\"A111U008_0_001\":\"\"},{\"A220U029_0_001\":\"[5,null]\"}]![{\"A121U002_0_001\":\"\",\"A121U002_0_002\":\"\",\"A121U002_0_007\":\"\"},{\"A111U008_0_001\":\"\"},{\"A221U024_0_001\":\"\"},{\"C120U007_2_001\":\"[1,null]\"},{\"C120U007_2_002\":\"[1,null]\"},{\"C120U007_2_003\":\"[1,null]\"},{\"C120U007_2_004\":\"[1,null]\"},{\"C120U007_2_005\":\"[1,null]\"},{\"C120U007_2_006\":\"[1,null]\"},{\"C120U007_2_008\":\"[1,null]\"},{\"C120U007_2_010\":\"[1,null]\"},{\"C120U007_2_011\":\"[1,null]\"},{\"C120U007_2_012\":\"[1,null]\"},{\"C120U007_2_013\":\"[1,null]\"},{\"C120U007_2_014\":\"[1,null]\"},{\"C120U007_2_015\":\"[1,null]\"},{\"C120U007_2_016\":\"[1,null]\"}]";
            String analyzeValue = analyze("test",groupVaule);
            System.out.println(analyzeValue);

            *//*groupVaule = "[{\"B220H066_0_001\":\"[1,null]\"},{\"A220H090_0_001\":\"[200,null]\"},{\"A221H024_0_004\":\"\"}]";
            analyzeValue = analyze(groupVaule);
            System.out.println("analyzeValue2:"+analyzeValue);

            groupVaule = "[{\"A121U002_0_002\":\"\",\"A121U002_0_001\":\"\",\"A121U002_0_004\":\"\",\"A121U002_0_006\":\"\",\"A121U002_0_005\":\"\"},{\"A120U100_0_001\":\"\",\"A120U100_0_002\":\"\"},{\"A120U090_0_001\":\"\"},{\"A220U054_0_001\":\"[1,null]\"},{\"B220U039_0_001\":\"[null,7]\"}]";
            analyzeValue = analyze(groupVaule);
            System.out.println("analyzeValue3:"+analyzeValue);*//*

        }catch (Exception e){
            e.printStackTrace();
        }
    }*/

}
