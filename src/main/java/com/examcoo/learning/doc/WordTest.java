/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: WordTest
 * Author:   lida
 * Date:     2018/8/28 11:07
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 * lida         2018/8/28 11:07     V1.0              描述
 */
package com.examcoo.learning.doc;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 〈使用模板写word文档〉<br>
 *
 4.1使用freemarker实现我们的需求

 4.4.1 自己新建一个 word的模板 模板中指定需要填写的内容的字段 如 name， type等

 4.2.2.新建word文档模板之后，保存为xml格式的文件，保存之后打开编辑，在xml中修改我们的字段为${name}等，修改之后可以通过代码的方式来写入内容到wrod中

 4.2.3.利用freemarker 来实现 生成word、动态添加数据到word中
 *
 * @author lida
 * @create 2018/8/28
 * @since 1.0.0
 */
public class WordTest {


    private Configuration configuration = null;

    public WordTest(){
        configuration = new Configuration();
        configuration.setDefaultEncoding("UTF-8");
    }

    public static void main(String[] args) {
        WordTest test = new WordTest();
        test.createWord();
    }

    public void createWord(){
        Map dataMap=new HashMap();
        getData(dataMap);
        configuration.setClassForTemplateLoading(this.getClass(), "/template");  //FTL文件所存在的位置
        Template t=null;
        try {
            t = configuration.getTemplate("word.ftl"); //文件名
        } catch (IOException e) {
            e.printStackTrace();
        }
        File outFile = new File("E:/outFilessa"+Math.random()*10000+".doc");
        Writer out = null;
        try {
            out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        try {
            t.process(dataMap, out);
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getData(Map dataMap) {
//        dataMap.put("title", "标题");
//        dataMap.put("year", "2012");
//        dataMap.put("month", "2");
//        dataMap.put("day", "13");
//        dataMap.put("auditor", "唐鑫");
//        dataMap.put("phone", "13020265912");
//        dataMap.put("weave", "占文涛");
////		dataMap.put("number", 1);
////		dataMap.put("content", "内容"+2);
//
//        List list = new ArrayList();
//        for (int i = 0; i < 10; i++) {
//            Map map = new HashMap();
//            map.put("number", i);
//            map.put("content", "内容"+i);
//            list.add(map);
//        }
//
//
//        dataMap.put("list", list);


        dataMap.put("title", "个人信息");
        dataMap.put("name", "wuhui");
        dataMap.put("age", "18");
        dataMap.put("birthday", "2000-11-11");
        dataMap.put("address", "福建省福州市晋安区");
    }

}