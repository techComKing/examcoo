/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: QueList
 * Author:   lida
 * Date:     2018/8/23 15:54
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 * lida         2018/8/23 15:54     V1.0              描述
 */
package com.examcoo.learning;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.xml.internal.fastinfoset.stax.events.Util;
import org.apache.http.*;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.poi.xwpf.usermodel.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.*;

/**
 * 〈考试酷试题导出下载，未设置登录〉<br>
 * 〈前提：用户已经在页面登录，查看cookie信息，通过setCookieStore手工设置〉
 *
 * @author lida
 * @create 2018/8/23
 * @since 1.0.0
 */
public class QueList {
    /**
     * 创建CookieStore实例
      */
    static CookieStore cookieStore = null;
    /**
     * 记录每道题是否测试过，key为题的顺序号，不是题的编号，value存放“已测试过”，“未测试”
     */
    static Map<String, String> hasmap = new HashMap<String, String>();
    /**
     * 存放题库json
     */
    static Map<String, String> jsonmap = new HashMap<String, String>();
    /**
     * 200 OK 请求成功
     */
    static int RESPONSE_CODE_OK = 200;

    /**
     * 使用cookieStore方式发送请求
     * @param client
     * @param url
     * @return
     * @throws Exception
     */
    public static HttpResponse sendRequestByCookieStore(CloseableHttpClient client, String url) throws Exception {
        System.out.println("----sendRequestByCookieStore");
        HttpResponse httpResponse = null;
        // 使用cookieStore方式
//        System.out.println("cookie store:" + cookieStore.getCookies());

        HttpGet httpGet = new HttpGet(url);
        System.out.println("request line:" + httpGet.getRequestLine());
        try {
            // 执行get请求
            httpResponse = client.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {//在最外面关闭客户端
//            try {
//                // 关闭流并释放资源
//                client.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
        return httpResponse;
    }

    /**
     *  获得“查看答卷和成绩”的URL
     * @param httpResponse
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static List analysisResponse1(HttpResponse httpResponse)
            throws ParseException, IOException {
        //存放URL
        List list = new ArrayList();
        // 响应状态
        StatusLine statusLine = httpResponse.getStatusLine();
        if (statusLine.getStatusCode() == RESPONSE_CODE_OK) {
            // 获取响应消息实体
            HttpEntity entity = httpResponse.getEntity();
            // 判断响应实体是否为空
            if (entity != null) {
                String responseString = EntityUtils.toString(entity, HTTP.UTF_8);
                String html = responseString.replace("\r\n", "");
                Document doc = Jsoup.parse(html);
                //获得“查看答卷和成绩”的URL并存入到List中
                for (Element element : doc.select(".br-nopadding")) {
//                    System.out.println("------"+element);
//                    System.out.println(element.select("a").toString());
                    if ("查看答卷和成绩".equals(element.select("a").text())) {
//                        System.out.println(element.select("a").attr("href"));
                        /**
                         *  /class/paper/viewexam/cid/206571/tid/939538/xid/109/p/1
                         */
                        list.add(element.select("a").attr("href"));
                    }
                }
                //记录每道题是否测试过---这里逻辑有问题，不能正确判断
//                Elements elements = doc.select(".ui-li-examnum");
//                Elements elements2 = doc.select(".br-padding12");
//                System.out.println(elements2.size());
//                System.out.println(elements2);
//                for (int i=0; i<elements2.size();i++) {
//                    System.out.println(i+"----"+elements2.get(i).text());
//                }
//                for (int i=0; i<elements.size();i+=2) {
//                    System.out.println(i+"----"+elements.get(i).text());
//                    System.out.println(i/2+"----"+elements2.get(i/2).text());
//                    hasmap.put(elements.get(i).text(), elements2.get(i/2).text());
//                }
            }
        }
        return list;
    }

    /**
     *  找到“查看答卷”链接
     * @param httpResponse
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static List analysisResponse2(HttpResponse httpResponse)
            throws ParseException, IOException {
        List list = new ArrayList();
        // 响应状态
        StatusLine statusLine = httpResponse.getStatusLine();
        if (statusLine.getStatusCode() == RESPONSE_CODE_OK) {
            // 获取响应消息实体
            HttpEntity entity = httpResponse.getEntity();
            // 判断响应实体是否为空
            if (entity != null) {
                String responseString = EntityUtils.toString(entity, HTTP.UTF_8);
                String html = responseString.replace("\r\n", "");
                Document doc = Jsoup.parse(html);
                String url = doc.select("a").attr("href");
//                System.out.println("查看答卷url----:::"+url);
                if (!Util.isEmptyString(url)) {
                    /**
                     *  /editor/do/recur/id/53259303
                     */
                    list.add(url);
                }
            }
        }
        return list;
    }


    /**
     *  获得tokenleid
     * @param httpResponse
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static Map<String, Object> analysisResponse3(HttpResponse httpResponse)
            throws ParseException, IOException {
        /*用來封裝要保存的参数*/
        Map<String, Object> map = new HashMap<String, Object>();
        // 响应状态
        StatusLine statusLine = httpResponse.getStatusLine();
        if (statusLine.getStatusCode() == RESPONSE_CODE_OK) {
            // 获取响应消息实体
            HttpEntity entity = httpResponse.getEntity();
            // 判断响应实体是否为空
            if (entity != null) {
                String responseString = EntityUtils.toString(entity, HTTP.UTF_8);
                String html = responseString.replace("\r\n", "");
                Document doc = Jsoup.parse(html);
//                System.out.println("doc-----"+doc.getElementsByTag("script").eq(1));
                /*取得script下面的第二个script中的JS变量*/
                Elements elements = doc.getElementsByTag("script").eq(1);
                /*循环遍历script下面的JS变量*/
                for (Element element : elements) {
                    /**
                     * var baseUrl = "";var imgDomain = "https://img.examcoo.com/1444";var aliyunPubDomain = "https://img.examcoo.com";var fromAction = 'recur';var leid = "53266237";var tokenleid = "18a912692eda89fa2210ffcbad843793";var bPreview = 0 ;var uid = "3465324";
                     */
//                    System.out.println("ele------"+element.data().toString());;
                    /*取得JS变量数组*/
                    String[] data = element.data().split("var");
                    /*取得单个JS变量*/
                    for(String variable : data){
                        /*过滤variable为空的数据*/
                        if(variable.contains("=")){
                            String[] kvp = variable.split("=");
                            String kvp1 = kvp[1].replaceAll("\"","")
                                    .replaceAll("'","")
                                    .trim();
//                            System.out.println(kvp[0].trim()+"-----"+kvp1.substring(0,kvp1.length()-1));
                            map.put(kvp[0].trim(),kvp1.substring(0,kvp1.length()-1));
                        }
                    }
                }
            }
        }
        return map;
    }

    /**
     * 解析json，将试题与答案存放在同一个容器中
     * @param client
     * @param baseUrl
     * @param httpResponse
     * @param rowNum
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static List analysisResponse4_1(CloseableHttpClient client, String baseUrl, HttpResponse httpResponse, String rowNum)
            throws ParseException, IOException {
        List list = new ArrayList<>();
        String title = "";
        // 响应状态
        StatusLine statusLine = httpResponse.getStatusLine();
        if (statusLine.getStatusCode() == 200) {
            // 获取响应消息实体
            HttpEntity entity = httpResponse.getEntity();
            // 判断响应实体是否为空
            if (entity != null) {
                String responseString = EntityUtils.toString(entity, HTTP.UTF_8);
                String json = responseString.replace("\r\n", "");
                JSONObject jsonObject = JSONObject.parseObject(json);
                for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                    System.out.println(entry.getKey()+"------"+entry.getValue());
                    if ("c".equals(entry.getKey())) {
                        JSONArray jarr = (JSONArray) entry.getValue();
                        int ques_no = 0;
                        for (Iterator iterator = jarr.iterator(); iterator.hasNext();) {
                            JSONObject job = (JSONObject) iterator.next();
                            String id = (String) job.get("id");
                            System.out.println("id===="+id);
//                            list.add("id: "+job.get("id"));
                            if ("b".equals(id)) {//题号
                                /**
                                 *
                                 {
                                 "id": "b",
                                 "c": [{
                                 "c": "三、B1型题（111-150）",
                                 "p": "0"
                                 }, {
                                 "c": "以下两题共用备选答案",
                                 "p": "0"
                                 }, {
                                 "c": "A.桑菊饮合苇茎汤",
                                 "p": "0"
                                 }, {
                                 "c": "B.麻杏石甘汤合苇茎汤",
                                 "p": "0"
                                 }, {
                                 "c": "C.清营汤合四逆汤",
                                 "p": "0"
                                 }, {
                                 "c": "D.生脉散合四逆汤",
                                 "p": "0"
                                 }, {
                                 "c": "E.竹叶石膏汤",
                                 "p": "0"
                                 }]
                                 }

                                 {
                                 "id": "b",
                                 "c": [
                                 {
                                 "c": "（以下两题共用备选答案）",
                                 "p": "0"
                                 },
                                 {
                                 "c": "A.风邪",
                                 "p": "0"
                                 },
                                 {
                                 "c": "B.寒邪",
                                 "p": "0"
                                 },
                                 {
                                 "c": "C.湿邪",
                                 "p": "0"
                                 },
                                 {
                                 "c": "D.燥邪",
                                 "p": "0"
                                 },
                                 {
                                 "c": "E.火邪",
                                 "p": "0"
                                 }
                                 ]
                                 }

                                 {
                                 "id": "b",
                                 "c": [
                                 {
                                 "c": "<u>第二十九单元  妇儿科病证的针灸治疗</u>",
                                 "p": "0"
                                 },
                                 {
                                 "c": "<u>细目二：痛经</u>",
                                 "p": "0"
                                 }
                                 ]
                                 }
                                 */
                                JSONArray jsonArray = (JSONArray)job.get("c");
                                for (Iterator iter = jsonArray.iterator(); iter.hasNext();) {
                                    JSONObject jsonObj = (JSONObject) iter.next();
                                    list.add(jsonObj.get("c").toString().replaceAll("&nbsp;","  ").trim());
                                }
                            } else if (id.startsWith("s1_")) {//试题
                                ques_no++;
                                //题目
                                list.add(ques_no+". "+job.get("a")+"["+job.get("d")+"分]");
                                //选项
                                JSONArray jsonArray = JSONArray.parseArray((String) job.get("b"));
//                                System.out.println(jsonArray);
                                char anser_no = 'A';
                                for (Iterator iter = jsonArray.iterator(); iter.hasNext();) {
                                    JSONObject jsonObj = (JSONObject) iter.next();
                                    list.add(anser_no+" "+jsonObj.get("o").toString().replaceAll("&nbsp;","  ").trim());//將空格&nbsp;替換掉
                                    anser_no++;
                                }
                                //参考答案
                                list.add("参考答案: "+getRigthAnserNo(Double.parseDouble(job.get("c").toString())));
                                //查看解析
                                String questionNo = id.split("_")[1];
                                list.add("查看解析: "+getAnalysis(client, baseUrl, questionNo));
                            } else {//标题
                                /**
                                 {
                                 "id": "750069",
                                 "a": "【中西·执医】新阳光2018模拟试题（三）第三单元",
                                 "b": "770083",
                                 "c": "新阳光辅导老师",
                                 "d": "新阳光教育发展有限公司",
                                 "e": "150",
                                 "f": "1531538280",
                                 "g": "1531538280",
                                 "h": "120",
                                 "i": "1",
                                 "j": "1",
                                 "l": "0",
                                 "m": "0",
                                 "n": "0",
                                 "o": "{\"a\":\"姓名\",\"b\":\"单元\",\"c\":\"准考证号\"}",
                                 "p": "0"
                                 }
                                 */
                                list.add(job.get("a"));
                                title = (String) job.get("a");
                                list.add("试卷编号: "+job.get("id"));
                                list.add("录入者　: "+job.get("c")+"（"+job.get("d")+"）");
                                list.add("试卷总分: "+job.get("e"));
                                list.add("答题时间: "+job.get("h")+"分钟");
//                                System.out.println("%%%%%%%%%%-------"+job.get("o"));
                                if (job.get("o")!=null && !"".equals(job.get("o"))){
                                    JSONObject obj = JSONObject.parseObject((String) job.get("o"));
                                    list.add("答题时间: "+obj.get("a")+": _______________"+obj.get("b")+": _______________"+obj.get("c")+": _______________");
                                } else {
                                    list.add("答题时间: 姓名: _______________单元: _______________准考证号: _______________");
                                }
                            }
                        }
                    }
                }
                jsonmap.put("[json]["+rowNum+"]"+title.replaceAll("/",""), json);//文件名称中不能有斜杠/
            }
        }
        return list;
    }

    /**
     * 解析json，将试题与答案分开存放
     * @param client
     * @param baseUrl
     * @param httpResponse
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static Map analysisResponse4_2(CloseableHttpClient client, String baseUrl, HttpResponse httpResponse)
            throws ParseException, IOException {
        Map map = new HashMap();
        List questionList = new ArrayList<>();
        List answerList = new ArrayList<>();
        map.put("one",questionList);
        map.put("two",answerList);
        // 响应状态
        StatusLine statusLine = httpResponse.getStatusLine();
        if (statusLine.getStatusCode() == 200) {
            // 获取响应消息实体
            HttpEntity entity = httpResponse.getEntity();
            // 判断响应实体是否为空
            if (entity != null) {
                String responseString = EntityUtils.toString(entity, HTTP.UTF_8);
                String json = responseString.replace("\r\n", "");

                JSONObject jsonObject = JSONObject.parseObject(json);
                for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                    System.out.println(entry.getKey()+"------"+entry.getValue());
                    if ("c".equals(entry.getKey())) {
                        JSONArray jarr = (JSONArray) entry.getValue();
                        int ques_no = 0;
                        for (Iterator iterator = jarr.iterator(); iterator.hasNext();) {
                            JSONObject job = (JSONObject) iterator.next();
                            String id = (String) job.get("id");
                            System.out.println("id===="+id);
//                            list.add("id: "+job.get("id"));
                            if ("b".equals(id)) {//题号
                                /**
                                 *
                                 {
                                 "id": "b",
                                 "c": [{
                                 "c": "三、B1型题（111-150）",
                                 "p": "0"
                                 }, {
                                 "c": "以下两题共用备选答案",
                                 "p": "0"
                                 }, {
                                 "c": "A.桑菊饮合苇茎汤",
                                 "p": "0"
                                 }, {
                                 "c": "B.麻杏石甘汤合苇茎汤",
                                 "p": "0"
                                 }, {
                                 "c": "C.清营汤合四逆汤",
                                 "p": "0"
                                 }, {
                                 "c": "D.生脉散合四逆汤",
                                 "p": "0"
                                 }, {
                                 "c": "E.竹叶石膏汤",
                                 "p": "0"
                                 }]
                                 }

                                 {
                                 "id": "b",
                                 "c": [
                                 {
                                 "c": "（以下两题共用备选答案）",
                                 "p": "0"
                                 },
                                 {
                                 "c": "A.风邪",
                                 "p": "0"
                                 },
                                 {
                                 "c": "B.寒邪",
                                 "p": "0"
                                 },
                                 {
                                 "c": "C.湿邪",
                                 "p": "0"
                                 },
                                 {
                                 "c": "D.燥邪",
                                 "p": "0"
                                 },
                                 {
                                 "c": "E.火邪",
                                 "p": "0"
                                 }
                                 ]
                                 }

                                 {
                                 "id": "b",
                                 "c": [
                                 {
                                 "c": "<u>第二十九单元  妇儿科病证的针灸治疗</u>",
                                 "p": "0"
                                 },
                                 {
                                 "c": "<u>细目二：痛经</u>",
                                 "p": "0"
                                 }
                                 ]
                                 }
                                 */
                                JSONArray jsonArray = (JSONArray)job.get("c");
                                for (Iterator iter = jsonArray.iterator(); iter.hasNext();) {
                                    JSONObject jsonObj = (JSONObject) iter.next();
                                    questionList.add(jsonObj.get("c").toString().replaceAll("&nbsp;","  ").trim());
                                }
                            } else if (id.startsWith("s1_")) {//试题
                                ques_no++;
                                //题目
                                questionList.add(ques_no+". "+job.get("a")+"["+job.get("d")+"分]");
                                //选项
                                JSONArray jsonArray = JSONArray.parseArray((String) job.get("b"));
//                                System.out.println(jsonArray);
                                char anser_no = 'A';
                                for (Iterator iter = jsonArray.iterator(); iter.hasNext();) {
                                    JSONObject jsonObj = (JSONObject) iter.next();
                                    questionList.add(anser_no+" "+jsonObj.get("o").toString().replaceAll("&nbsp;","  ").trim());//將空格&nbsp;替換掉
                                    anser_no++;
                                }
                                questionList.add("我的答案: ");

                                //参考答案
                                answerList.add(ques_no+". 答案: "+getRigthAnserNo(Double.parseDouble(job.get("c").toString())));
                                //查看解析
                                String questionNo = id.split("_")[1];
                                answerList.add("解析: "+getAnalysis(client, baseUrl, questionNo));
                            } else {//标题
                                /**
                                 {
                                 "id": "750069",
                                 "a": "【中西·执医】新阳光2018模拟试题（三）第三单元",
                                 "b": "770083",
                                 "c": "新阳光辅导老师",
                                 "d": "新阳光教育发展有限公司",
                                 "e": "150",
                                 "f": "1531538280",
                                 "g": "1531538280",
                                 "h": "120",
                                 "i": "1",
                                 "j": "1",
                                 "l": "0",
                                 "m": "0",
                                 "n": "0",
                                 "o": "{\"a\":\"姓名\",\"b\":\"单元\",\"c\":\"准考证号\"}",
                                 "p": "0"
                                 }
                                 */
                                questionList.add(job.get("a"));
                                answerList.add(job.get("a"));
                                questionList.add("试卷编号: "+job.get("id"));
                                answerList.add("试卷编号: "+job.get("id"));
                                questionList.add("录入者　: "+job.get("c")+"（"+job.get("d")+"）");
                                answerList.add("录入者　: "+job.get("c")+"（"+job.get("d")+"）");
                                questionList.add("试卷总分: "+job.get("e"));
                                answerList.add("试卷总分: "+job.get("e"));
                                questionList.add("答题时间: "+job.get("h")+"分钟");
                                answerList.add("答题时间: "+job.get("h")+"分钟");
//                                System.out.println("%%%%%%%%%%-------"+job.get("o"));
                                if (job.get("o")!=null && !"".equals(job.get("o"))){
                                    JSONObject obj = JSONObject.parseObject((String) job.get("o"));
                                    questionList.add("答题时间: "+obj.get("a")+": _______________"+obj.get("b")+": _______________"+obj.get("c")+": _______________");
                                    answerList.add("答题时间: "+obj.get("a")+": _______________"+obj.get("b")+": _______________"+obj.get("c")+": _______________");
                                } else {
                                    questionList.add("答题时间: 姓名: _______________单元: _______________准考证号: _______________");
                                    answerList.add("答题时间: 姓名: _______________单元: _______________准考证号: _______________");
                                }
                            }
                        }
                    }
                }
            }
        }
        return map;
    }

    /**
     * 获得解析
     * https://www.examcoo.com/editor/comment/getcomment/type/0/id/333949184015208451/tokenpid/notsupport
     * @param questionNo 333949184015208451
     * @return
     */
    public static String getAnalysis(CloseableHttpClient client, String baseUrl, String questionNo){
        String result = "";
        /**
         * https://www.examcoo.com/editor/comment/getcomment/type/0/id/333949184015208451/tokenpid/notsupport
         */
        String url = baseUrl+"/editor/comment/getcomment/type/0/id/"+questionNo+"/tokenpid/notsupport";
        //发送请求
        try {
            HttpResponse httpResponse = sendRequestByCookieStore(client, url);
//            printResponse(httpResponse);
            result = analysisResponse5(httpResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     *  找到题目解析内容
     * @param httpResponse
     * @return
     * @throws ParseException
     * @throws IOException
     */
    public static String analysisResponse5(HttpResponse httpResponse)
            throws ParseException, IOException {
        String result = "";
        // 响应状态
        StatusLine statusLine = httpResponse.getStatusLine();
        if (statusLine.getStatusCode() == 200) {
            // 获取响应消息实体
            HttpEntity entity = httpResponse.getEntity();
            // 判断响应实体是否为空
            if (entity != null) {
                String responseString = EntityUtils.toString(entity, HTTP.UTF_8);
                result = responseString.replace("\r\n", "");
            }
        }
        return result;
    }

    /**
     * 将答案数值转成字母
         1    A
         2    B
         4    C
         8    D
         16   E
     * @param m
     * @return
     */
    public static char getRigthAnserNo(Double m){
        double n = 2;//
        Double value = Math.log(m) / Math.log(n);
        char[] arr = Character.toChars(value.intValue()+65);
        return arr[0];
    }

    /**
     * 打印响应数据
     * EntityUtils.toString(entity)方法会中断stream流，导致stream close
     * @param httpResponse
     * @throws ParseException
     * @throws IOException
     */
    public static void printResponse(HttpResponse httpResponse)
            throws ParseException, IOException {
        // 获取响应消息实体
        HttpEntity entity = httpResponse.getEntity();
        // 响应状态
        System.out.println("status:" + httpResponse.getStatusLine());
        System.out.println("headers:");
        HeaderIterator iterator = httpResponse.headerIterator();
        while (iterator.hasNext()) {
            System.out.println("\t" + iterator.next());
        }
        // 判断响应实体是否为空
        if (entity != null) {
            /**
             * TODO You can't read the response stream twice, which is what you are doing by effectively invoking
             */
            String responseString = EntityUtils.toString(entity);
            System.out.println("response length:" + responseString.length());
            System.out.println("response content:"
                    + responseString.replace("\r\n", ""));
        }
    }

    /**
     * lastuid=3465324; lastspl=29c37c28075b95cbb839330c7801bf53; PHPSESSID=v9nmeh3h6kh3q12p6vb1pa2116
     * 设置cookie
     */
    public static void setCookieStore() {
        System.out.println("----setCookieStore");
        cookieStore = new BasicCookieStore();
        // 新建一个Cookie
        BasicClientCookie cookie1 = new BasicClientCookie("lastuid","3465324");
        cookie1.setDomain("www.examcoo.com");
        BasicClientCookie cookie2 = new BasicClientCookie("lastspl","a4d9eba5bd36196dcec6951722a90170");//会变
        cookie2.setDomain("www.examcoo.com");
        BasicClientCookie cookie3 = new BasicClientCookie("PHPSESSID","mmevvb0f7lj84kp3agpim0p0p3");//会变
        cookie3.setDomain("www.examcoo.com");
        cookieStore.addCookie(cookie1);
        cookieStore.addCookie(cookie2);
        cookieStore.addCookie(cookie3);
    }

    /**
     * 存储成文本文件
     * @param list 数据集
     * @param path 文件路径 E:/xyg/
     * @param fileName 文件名称
     */
    public static void storeTxt(List list, String path, String fileName) {
        String fileSuffix = ".txt";
        try ( BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(path+fileName+fileSuffix)), "UTF-8"))){
            for (Object object : list) {
                bw.write(object.toString().replaceAll("<u>","").replaceAll("</u>",""));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 存储成文本文件
     * @param str json
     * @param path 文件路径 E:/xyg/
     * @param fileName 文件名称
     */
    public static void storeJsonTxt(String str, String path, String fileName) {
        String fileSuffix = ".txt";
        try ( BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(path+fileName+fileSuffix)), "UTF-8"))){
            bw.write(str);
            bw.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  存储为word文件
     *  题库与答案在一起
     * @param list 数据集
     * @param path 文件路径 E:/xyg/
     * @param fileName 文件名称
     */
    public static void storeDocAll(List list, String path, String fileName) {
        String fileSuffix = ".doc";
        //新建一个文档
        XWPFDocument doc = new XWPFDocument();
        //创建一个段落
        XWPFParagraph para = doc.createParagraph();
        para.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = para.createRun();
        run.setBold(true);
        run.setFontSize(14);
        run.setText(list.get(0).toString());
        run.addBreak();

        para = doc.createParagraph();
        para.setAlignment(ParagraphAlignment.LEFT);
        //一个XWPFRun代表具有相同属性的一个区域。
        for (int k=1; k<list.size(); k++) {
            Object object = list.get(k);
            String line = object.toString();
            run = para.createRun();
            if (line.contains("型题")) {
                run.setBold(true);//字体加粗
            }
            if (line.contains("参考答案")) {
                run.setColor("C0C0C0");//silver
            }
            if (line.contains("查看解析")) {
                run.setColor("A9A9A9");//darkgray
            }
            if (line.contains("<u>")) {
                line = line.replaceAll("<u>","").replaceAll("</u>","");
//                System.out.println(line);
                run.setUnderline(UnderlinePatterns.SINGLE);//单下划线
                run.setFontSize(11);
            }
            run.setText(line);
            run.addBreak();
            if (k == 5) {
                run.addBreak();
            }
        }
        try {
            OutputStream os = new FileOutputStream(path+fileName+fileSuffix);
            doc.write(os);
            close(os);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *  存储为word文件
     *  题库与答案分开存储
     * @param list 数据集
     * @param path 文件路径 E:/xyg/
     * @param fileName 文件名称
     */
    public static void storeDoc(List list, String path, String fileName) {
        String fileSuffix = ".doc";
        //新建一个文档
        XWPFDocument doc = new XWPFDocument();
        //创建一个段落
        XWPFParagraph para = doc.createParagraph();
        para.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun run = para.createRun();
        run.setBold(true);
        run.setFontSize(14);
        run.setText(list.get(0).toString());
        run.addBreak();

        para = doc.createParagraph();
        para.setAlignment(ParagraphAlignment.LEFT);
        //一个XWPFRun代表具有相同属性的一个区域。
        for (int k=1; k<list.size(); k++) {
            Object object = list.get(k);
            String line = object.toString();
            run = para.createRun();
            if (line.contains("型题")) {
                run.setBold(true);//字体加粗
            }
            if (line.contains("<u>")) {
                line = line.replaceAll("<u>","").replaceAll("</u>","");
//                System.out.println(line);
                run.setUnderline(UnderlinePatterns.SINGLE);//单下划线
                run.setFontSize(11);
            }
            if (line.contains("解析:")) {
                run.setColor("808080");//gray
            }
            if (line.contains("我的答案:")) {
                run.setColor("1E90FF");//dodgerblue
            }
            run.setText(line);
            run.addBreak();
            if (k == 5) {
                run.addBreak();
            }
        }
        try {
            OutputStream os = new FileOutputStream(path+fileName+fileSuffix);
            doc.write(os);
            close(os);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 关闭输出流
     * @param os
     */
    private static void close(OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     题库列表URL：https://www.examcoo.com/class/paper/index/cid/206571/p/1
     查看答案和成绩URL：https://www.examcoo.com/class/paper/viewexam/cid/206571/tid/939538/xid/109/p/1
     成绩列表URL：https://www.examcoo.com/class/paper/listdetail/cid/206571/purpose/1/tid/939538/xid/109/idpaging/0
     查看答卷URL：https://www.examcoo.com/editor/do/recur/id/53259303
     试卷题库URL：https://www.examcoo.com/editor/rpc/getexamcontent/leid/53259303/tokenleid/24b8f6e5f28fe233115f0ad1b1d7dee7
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String baseUrl = "https://www.examcoo.com";
        /**
         * https://www.examcoo.com/class/paper/index/cid/206571/p/1
         */
        int totalPage = 19;//总页数19
        for (int page=1; page<=totalPage; page++) {
            String firstUrl = baseUrl+"/class/paper/index/cid/206571/p/"+page;
            //设置cookie
            setCookieStore();
            //初始化client
            CloseableHttpClient client = HttpClients.custom().setDefaultCookieStore(cookieStore).build();
            try {
                //发送请求
                HttpResponse httpResponse = sendRequestByCookieStore(client, firstUrl);
//                printResponse(httpResponse);
                //解析响应报文
                List<String> list = analysisResponse1(httpResponse);
                for (String url: list) {
                    String[] arr = url.split("/");
                    String anserNo = arr[7];
                    String rowNum = arr[9];//顺序编号
                    /**
                     * https://www.examcoo.com/class/paper/listdetail/cid/206571/purpose/1/tid/939538/xid/109/idpaging/0
                     */
                    String secondUrl = baseUrl+"/class/paper/listdetail/cid/206571/purpose/1/tid/"+anserNo+"/xid/"+rowNum+"/idpaging/0";
                    HttpResponse httpResponse2 = sendRequestByCookieStore(client, secondUrl);
//                    printResponse(httpResponse2);
                    //解析响应报文
                    List<String> list2 = analysisResponse2(httpResponse2);
                    System.out.println(list2.size());
                    for (String url2: list2) {
                        /**
                         * https://www.examcoo.com/editor/do/recur/id/53259303
                         */
                        url2 = baseUrl+url2;
                        System.out.println("查看答卷获取tokenleid--"+url2);
                        //发送请求
                        HttpResponse httpResponse3 = sendRequestByCookieStore(client, url2);
//                        printResponse(httpResponse3);
                        //解析响应报文
                        Map map = analysisResponse3(httpResponse3);
                        String leid = (String) map.get("leid");
                        String tokenleid = (String) map.get("tokenleid");
                        /**
                         * https://www.examcoo.com/editor/rpc/getexamcontent/leid/53259303/tokenleid/24b8f6e5f28fe233115f0ad1b1d7dee7
                         */
                        String lastUrl = baseUrl+"/editor/rpc/getexamcontent/leid/"+leid+"/tokenleid/"+tokenleid;
                        //发送请求
                        HttpResponse httpResponse4 = sendRequestByCookieStore(client, lastUrl);
//                        printResponse(httpResponse4);

                        /**
                         *  导出数据-将试题与答案存放在同一个文件中
                         */
                        //解析响应报文
//                        List list4 = analysisResponse4_1(client, baseUrl, httpResponse4, rowNum);
//                        String fileName = "["+rowNum+"]"+list4.get(0).toString().replaceAll("/","");//文件名称中不能有斜杠/
//                        String wordPath = "E:/xyg/考试酷-2018中西医全科无忧班-试题集（试题答案在一起）- word/";//路径要以/结束
//                        String txtPath = "E:/xyg/考试酷-2018中西医全科无忧班-试题集（试题答案在一起）- txt/";//路径要以/结束
//                        System.out.println(wordPath+fileName);
//                        //导出到文件中
//                        storeDocAll(list4, wordPath, fileName);//导出到word
//                        storeTxt(list4, txtPath, fileName);//导出到txt
                        /*for (Object object : list4) {
                            System.out.println(rowNum);
                            System.out.println(object);
                        }*/

                        /**
                         *  导出数据-将试题与答案分开存放
                         */
                        Map map1 = analysisResponse4_2(client, baseUrl, httpResponse4);
                        List questionList = (List) map1.get("one");
                        List answerList = (List) map1.get("two");
                        String filePath = "E:/xyg/考试酷-2018中西医全科无忧班-试题集（试题答案分开）/试题/";//路径要以/结束
                        String questionFileName = "["+rowNum+"]"+questionList.get(0).toString().replaceAll("/","")+"--题目";//文件名称中不能有斜杠/
                        storeDoc(questionList, filePath, questionFileName);//导出到word
                        filePath = "E:/xyg/考试酷-2018中西医全科无忧班-试题集（试题答案分开）/答案/";//路径要以/结束
                        String answerFileName = "["+rowNum+"]"+questionList.get(0).toString().replaceAll("/","")+"--答案";//文件名称中不能有斜杠/
                        storeDoc(answerList, filePath, answerFileName);//导出到word

                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    // 关闭流并释放资源
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
//        for (Map.Entry<String, String> entry : hasmap.entrySet()) {
//            System.out.println(entry.getKey()+":"+entry.getValue());
//        }
        //将json保存为文件
        for (Map.Entry entry : jsonmap.entrySet()) {
            String fileName = (String) entry.getKey();//文件名称中不能有斜杠/
            String path = "E:/xyg/考试酷-2018中西医全科无忧班-试题集（试题答案在一起）- json/";
            System.out.println(path+fileName);
            //导出到文件中
            storeJsonTxt((String) entry.getValue(), path, fileName);//导出到txt
        }
    }
}