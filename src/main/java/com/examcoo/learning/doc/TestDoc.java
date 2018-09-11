/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: TestDoc
 * Author:   lida
 * Date:     2018/8/27 17:54
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 * lida         2018/8/27 17:54     V1.0              描述
 */
package com.examcoo.learning.doc;

import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author lida
 * @create 2018/8/27
 * @since 1.0.0
 */
public class TestDoc {

    @RequestMapping("getDoc")
    public void getDoc(HttpServletRequest request, HttpServletResponse response) {
//        MessageVO vo = new MessageVO();
        Map<String,String> dataMap = new HashMap<String,String>();
        dataMap.put("title", "个人信息");
        dataMap.put("name", "wuhui");
        dataMap.put("age", "18");
        dataMap.put("birthday", "2000-11-11");
        dataMap.put("address", "福建省福州市晋安区");
        String newWordName = "信息.doc";
        //调用打印word的函数
        DocUtil.download(request, response, newWordName, dataMap);
    }

}