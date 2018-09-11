/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: Test2
 * Author:   lida
 * Date:     2018/8/27 11:31
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 * lida         2018/8/27 11:31     V1.0              描述
 */
package com.examcoo.learning;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.Iterator;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author lida
 * @create 2018/8/27
 * @since 1.0.0
 */
public class Test {
    public static void main(String[] args) {
        char anser_no = 65;
        anser_no++;
        System.out.println(anser_no);

        double n = 2;//底数
        double m = 1;//指数
        Double value = Math.log(m) / Math.log(n);
        System.out.println(value);
        System.out.println(value.longValue());
        System.out.println(value.byteValue());
        System.out.println(value.intValue());
        char[] arr = Character.toChars(value.intValue()+65);
        System.out.println(arr[0]);
//        for (char c : arr){
//            System.out.println(c);
//        }

        JSONArray jsonArray = JSONArray.parseArray("[{\"o\":\"  \"},{\"o\":\"  \"},{\"o\":\"  \"},{\"o\":\"  \"},{\"o\":\"  \"}]");
        for (Iterator iter = jsonArray.iterator(); iter.hasNext();) {
            JSONObject jsonObj = (JSONObject) iter.next();
            System.out.println("["+jsonObj.get("o").toString().trim()+"]");
        }

        String json = "{\"id\":\"661128\",\"a\":\"2018内科一（1-1、1-2慢阻肺、哮喘、肺炎、结核）\",\"b\":\"770083\",\"c\":\"新阳光辅导老师\",\"d\":\"新阳光教育发展有限公司\",\"e\":\"58\",\"f\":\"1521785082\",\"g\":\"1521785082\",\"h\":\"58\",\"i\":\"1\",\"j\":\"1\",\"l\":\"0\",\"m\":\"0\",\"n\":\"0\",\"o\":\"\",\"p\":\"0\"}";
        JSONObject jsonObject = JSONObject.parseObject(json);
        System.out.println(jsonObject.get("a"));
        System.out.println(jsonObject.get("c"));
        System.out.println("".equals(jsonObject.get("o")));

        String line = "<u>第五单元 &nbsp;拟肾上腺素药</u>";
        System.out.println(line.replaceAll("<u>","").replaceAll("</u>",""));
        System.out.println(line);
    }

}