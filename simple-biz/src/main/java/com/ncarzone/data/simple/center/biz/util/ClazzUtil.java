package com.ncarzone.data.simple.center.biz.util;

import com.google.common.collect.Maps;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 反射工具类
 */
@Data
public class ClazzUtil {

    /**
     * 获取类的属性和数据类型
     */
    public static Map<String, String> getFields(Object object)  throws Exception {
        Map<String, String> map = Maps.newLinkedHashMap();
        Field[] fields = object.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            String classType = field.getType().toString();
            int lastIndex = classType.lastIndexOf(".");
            classType = classType.substring(lastIndex + 1);
            map.put(field.getName(), classType);
        }
        return map;
    }
}
