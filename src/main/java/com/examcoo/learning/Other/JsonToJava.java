/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: jsonToJava
 * Author:   lida
 * Date:     2018/9/10 14:16
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 * lida         2018/9/10 14:16     V1.0              描述
 */
package com.examcoo.learning.Other;

import java.util.List;

/**
 * 〈GsonFormat插件〉<br>
 *  alt+s
 *
     {
     "name":"网站",
     "num":3,
     "sites":[ "Google", "Runoob", "Taobao" ]
     }
 * @author lida
 * @create 2018/9/10
 * @since 1.0.0
 */
public class JsonToJava {

    /**
     * name : 网站
     * num : 3
     * sites : ["Google","Runoob","Taobao"]
     */

    private String name;
    private int num;
    private List<String> sites;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public List<String> getSites() {
        return sites;
    }

    public void setSites(List<String> sites) {
        this.sites = sites;
    }
}