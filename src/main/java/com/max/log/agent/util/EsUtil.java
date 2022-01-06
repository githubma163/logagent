package com.max.log.agent.util;

//请求ES
public class EsUtil {

    public static boolean checkIndex(String url) {
        int statusCode = HttpUtil.sendHead(url);
        if (statusCode == 200) {
            return true;
        } else {
            return false;
        }
    }

    //创建索引
    public static String createIndex(String url, String content) {
        return HttpUtil.sendPut(url, content);
    }

    //保存数据
    public static String save(String url, String content) {
        return HttpUtil.sendPost(url, content);
    }

}
