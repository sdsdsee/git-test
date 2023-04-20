package com.eluolang.module.report.util;

import com.eluolang.module.report.model.Hw;
import com.eluolang.module.report.model.Vision;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class wordTest {
    public static void main(String[] args) throws IOException, TemplateException {
        Map<String, Object> user = new HashMap<>();
        user.put("name","张三");
        user.put("schoolName","xx学校");
        user.put("sex","男");
        user.put("className","六年级一班");
        List<Vision> visionList = new ArrayList<>();
        Vision vision = new Vision();
        vision.setName("裸眼视力");
        vision.setLeft("5.0");
        vision.setRight("5.0");
        vision.setComment("近视");
        visionList.add(vision);
        Vision vision1 = new Vision();
        vision1.setName("屈光轴径");
        vision1.setLeft("5.0");
        vision1.setRight("5.0");
        vision1.setComment("近视");
        visionList.add(vision1);
        Vision vision2 = new Vision();
        vision2.setName("屈光球镜");
        vision2.setLeft("5.0");
        vision2.setRight("5.0");
        vision2.setComment("远视");
        visionList.add(vision2);
        user.put("visionList",visionList);
        List<Hw> hwList = new ArrayList<>();
        Hw hw = new Hw();
        hw.setHeight("175.0");
        hw.setWeight("60.0");
        hw.setBmi("27.0");
        hw.setComment("正常");
        hwList.add(hw);
        user.put("hwList",hwList);

/*
        dataMap.put("name", "Tom");

        dataMap.put("age", "20");

        dataMap.put("province", "江苏");
        Map<String, String> dataMap2 = new HashMap<String, String>();

        dataMap2.put("name", "Tom");

        dataMap2.put("age", "20");

        dataMap2.put("province", "江苏");
        list.add(dataMap);
        list.add(dataMap2);*/
        Configuration configuration = new Configuration();

        configuration.setDefaultEncoding("utf-8");
        configuration.setClassicCompatible(true);
        configuration.setDirectoryForTemplateLoading(new File("C:\\Users\\Administrator\\Desktop\\新建文件夹"));
        configuration.setClassForTemplateLoading(wordTest.class, "/templates");
// 输出文档路径及名称

        File outFile = new File("C:\\Users\\Administrator\\Desktop\\新建文件夹\\test-gen.doc");

//以utf-8的编码读取ftl文件

        Template t = configuration.getTemplate("personal_report.ftl", "utf-8");

        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), "utf-8"), 10240);

        t.process(user, out);

        out.close();
    }
}
