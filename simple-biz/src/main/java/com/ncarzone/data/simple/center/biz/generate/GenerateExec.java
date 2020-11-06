package com.ncarzone.data.simple.center.biz.generate;


import com.ncarzone.data.simple.center.biz.enums.DbTypeEnum;
import com.ncarzone.data.simple.center.biz.enums.JavaTypeEnum;
import com.ncarzone.data.simple.center.biz.util.ClazzUtil;
import org.apache.commons.lang3.StringUtils;
import java.util.Map;

/**
 * 根据类生成表结构
 *
 * 使用方式
 * 1.将你的类字段copy到GenerateTable.java中
 * 2.执行GenerateExec.main方法
 * 3.控制台即可见生成的表结构
 *
 */
public class GenerateExec {

    /**
     * 根据类生成对应的表结构
     * @return
     * @throws Exception
     */
    public static String generateTable() throws Exception {
        StringBuffer buffer = new StringBuffer();
        // 获取字段名和类型
        Map<String, String> map = ClazzUtil.getFields(new GenerateTable());

        System.out.println(map);
        // 生成表结构
        return null;
    }

    /**
     * 生成表结构
     */
    private static String generateTable(Object object) throws Exception {
        StringBuffer buffer = new StringBuffer();
        buffer.append("CREATE TABLE `test` (");
        buffer.append("\n\t");
        buffer.append("`id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增Id',");
        buffer.append("\n\t");

        Map<String, String> noteMap = getFieldNote(object);
        Map<String, String> map = ClazzUtil.getFields(object);
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String field = entry.getKey();
            String type = entry.getValue();
            String mapperField = mapperField(field);
            String mapperType = mapperType(type);

            buffer.append("`" + mapperField + "` ");
            buffer.append(mapperType + " ");
            buffer.append("NOT NULL COMMENT ");
            buffer.append(" '" + noteMap.get(field) + "',");
            buffer.append("\n\t");

        }
        buffer.append("`create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',");
        buffer.append("\n\t");
        buffer.append("`create_person` varchar(20) NOT NULL DEFAULT 'system' COMMENT '创建人',");
        buffer.append("\n\t");
        buffer.append("`update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP COMMENT '修改时间',");
        buffer.append("\n\t");
        buffer.append("`update_person` varchar(20) NOT NULL DEFAULT 'system' COMMENT '更新人',");
        buffer.append("\n\t");
        buffer.append("PRIMARY KEY (`id`)");
        buffer.append("\n\t");
        buffer.append(") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='test表结构';");


        return buffer.toString();
    }
    /**
     * 获取字段注释
     */
    private static Map<String, String> getFieldNote(Object object) {
        GenerateDoc doclet = new GenerateDoc(getAbsolutePath(object));
        return doclet.exec();
    }
    /**
     * 获取java文件绝对路径
     */
    private static String getAbsolutePath(Object object) {
        StringBuffer buffer = new StringBuffer();

        String path= object.getClass().getResource("").getPath().replace("target/classes","src/main/java");

        buffer.append(path);
        buffer.append("/");
        buffer.append(object.getClass().getSimpleName());
        buffer.append(".java");
        return buffer.toString();
    }

    /**
     * 字段映射
     */
    private static String mapperField(String javaField) throws Exception {
        if (StringUtils.isBlank(javaField)) {
            throw new Exception("暂不支持javaField" + javaField);
        }

        StringBuffer buffer = new StringBuffer();
        int length = javaField.length();
        for (int i = 0 ; i < length; i++) {
            char item = javaField.charAt(i);
            if (Character.isUpperCase(item)) {
                buffer.append("_").append(Character.toLowerCase(item));
            } else {
                buffer.append(item);
            }
        }
        return buffer.toString();
    }


    /**
     * 类型映射
     */
    private static String mapperType(String javaType) throws Exception {
        if (StringUtils.isBlank(javaType)) {
            throw new Exception("暂不支持javaType" + javaType);
        }


        if (JavaTypeEnum.STRING.getName().equalsIgnoreCase(javaType)) {
            return DbTypeEnum.VARCHAR.getName();
        }

        if (JavaTypeEnum.DOUBLE.getName().equalsIgnoreCase(javaType)){
            return DbTypeEnum.DECIMAL.getName();
        }

        if (JavaTypeEnum.LONG.getName().equalsIgnoreCase(javaType)) {
            return DbTypeEnum.BIGINT.getName();
        }

        if (JavaTypeEnum.INT.getName().equalsIgnoreCase(javaType) || JavaTypeEnum.INTEGER.getName().equalsIgnoreCase(javaType)) {
            return DbTypeEnum.INT.getName();
        }

        if (JavaTypeEnum.DATE.getName().equalsIgnoreCase(javaType)) {
            return DbTypeEnum.DATETIME.getName();
        }
        if (JavaTypeEnum.BOOLEAN.getName().equalsIgnoreCase(javaType)) {
            return DbTypeEnum.TINYINT.getName();
        }

        throw new Exception("暂不支持javaType" + javaType);
    }




    public static void main(String[] args)  throws Exception {

        System.out.println(generateTable(new GenerateTable()));
    }

}
