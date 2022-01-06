package com.max.log.agent.util;

import com.max.log.agent.Log;
import com.max.log.agent.constant.Constant;

import java.util.List;
import java.util.Map;

public class LogESUtil {

    public static final String BULK_SAVE_INDEX = "{\"index\":{}}";

    public static void createIndex(Map<String, String> map, String content) {
        if (null == map || map.size() == 0) {
            return;
        }
        String url = map.get(Constant.URL);
        String app = map.get(Constant.APP);
        if (map.get(Constant.SEND).equals("es") && StringUtils.isNotBlank(app) && StringUtils.isNotBlank(url)) {
            if (url.endsWith("/")) {
                url += app;
            } else {
                url += "/" + app;
            }
            Boolean exists = EsUtil.checkIndex(url);
            if (exists) {
                System.out.println(app + " index already exists");
                return;
            }
            String sendResult = EsUtil.createIndex(url, content);
            System.out.println(app + " create index success " + sendResult);
        }
    }

    public static void saveLog(Map<String, String> map, Log log) {
        if (null == map || map.size() == 0) {
            return;
        }
        String url = map.get(Constant.URL);
        String app = map.get(Constant.APP);
        if (map.get(Constant.SEND).equals("es") && StringUtils.isNotBlank(app) && StringUtils.isNotBlank(url)) {
            if (url.endsWith("/")) {
                url += app;
            } else {
                url += "/" + app + "/" + app;
            }
            String sendResult = EsUtil.save(url, log.toString());
            System.out.println(log.toString() + "," + sendResult);
        }
    }

    //批量保存
    public static void bulkSaveLog(Map<String, String> map, List<Log> logList) {
        if (null == map || map.size() == 0) {
            return;
        }
        if (null == logList || logList.size() == 0) {
            return;
        }
        String url = map.get(Constant.URL);
        String app = map.get(Constant.APP);
        if (map.get(Constant.SEND).equals("es") && StringUtils.isNotBlank(app) && StringUtils.isNotBlank(url)) {
            if (url.endsWith("/")) {
                url += app;
            } else {
                url += "/" + app + "/" + app + "/_bulk";
            }
            StringBuilder sb = new StringBuilder();
            for (Log log : logList) {
                sb.append(BULK_SAVE_INDEX);
                sb.append("\n");
                sb.append(log.toString());
                sb.append("\n");
            }
            long startTime = System.currentTimeMillis();
            String sendResult = EsUtil.save(url, sb.toString());
            long endTime = System.currentTimeMillis();
            System.out.println("send es success,time:" + (endTime - startTime) + "ms");
            //System.out.println(sb.toString() + "," + sendResult);
        }
    }

}
