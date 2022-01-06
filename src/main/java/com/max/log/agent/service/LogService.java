package com.max.log.agent.service;

import com.max.log.agent.Log;
import com.max.log.agent.constant.Constant;
import com.max.log.agent.constant.DataSourceType;
import com.max.log.agent.util.ConfigUtil;
import com.max.log.agent.util.DateUtil;
import com.max.log.agent.util.LogESUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LogService {

    public static void createDataSource() {
        String dataSource = ConfigUtil.CONFIG_MAP.get(Constant.SEND);
        if (dataSource.equalsIgnoreCase(DataSourceType.ES.name())) {
            LogESUtil.createIndex(ConfigUtil.CONFIG_MAP, Constant.ES_CREATE_INDEX);
        }
    }

    public static void produce(String clazz, String method, Object[] args, long time) {
        System.out.println(clazz);
        System.out.println(method);
        List<Object> paramList = new ArrayList<>();
        for (Object object : args) {
            if (null != object) {
                paramList.add(object.toString().replaceAll("[\\t\\n\\r]", " "));
            } else {
                paramList.add(null);
            }
        }
        System.out.println(paramList);
        System.out.println(time);
        Log log = new Log();
        log.setClazz(clazz);
        log.setMethod(method);
        log.setParam(paramList.toString());
        log.setTime(time);
        log.setTimestmap(DateUtil.dateFormat(new Date()));
        produce(log);
    }

    public static void produce(Log log) {
        LogProduceConsumeService.getLogService().produce(log);
    }
}
