package com.max.log.agent.util;

import com.max.log.agent.Log;
import com.max.log.agent.constant.Constant;

import java.util.List;
import java.util.Map;

public class LogMongoUtil {

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
        if (StringUtils.isNotBlank(app) && StringUtils.isNotBlank(url)) {
            if (url.endsWith("/")) {
                url += app;
            } else {
                url += "/" + app + "/" + app + "/_bulk";
            }
            long startTime = System.currentTimeMillis();
            String sendResult = EsUtil.save(url, logList.toString());
            long endTime = System.currentTimeMillis();
            System.out.println("send mongo success,time:" + (endTime - startTime) + "ms");
            //System.out.println(sb.toString() + "," + sendResult);
        }
    }
}
