package com.ncarzone.data.simple.center.biz.generate;

import com.google.common.collect.Maps;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;
import com.sun.javadoc.RootDoc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 获取某一个java文件代码中属性对应的注释
 *
 */
public class GenerateDoc {

    public static Logger logger = LoggerFactory.getLogger(GenerateDoc.class);

    private static RootDoc rootDoc;
    private String javaBeanFilePath;

    public static boolean start(RootDoc root) {
        rootDoc = root;
        return true;
    }

    public GenerateDoc(String javaBeanFilePath) {
        this.javaBeanFilePath = javaBeanFilePath;
    }

    public Map<String, String> exec() {
        Map<String, String> map = Maps.newLinkedHashMap();

        com.sun.tools.javadoc.Main.execute(new String[]{"-doclet", GenerateDoc.class.getName(), "-docletpath",
                GenerateDoc.class.getResource("/").getPath(), "-encoding", "utf-8", javaBeanFilePath});
        ClassDoc[] classes = rootDoc.classes();

        if (classes == null || classes.length == 0) {
            logger.warn(javaBeanFilePath + " 无ClassDoc信息");
            return map;
        }

        ClassDoc classDoc = classes[0];
        // 获取属性名称和注释
        FieldDoc[] fields = classDoc.fields(false);
        for (FieldDoc field : fields) {
            map.put(field.name(), field.commentText());
        }

        return map;

    }
}
