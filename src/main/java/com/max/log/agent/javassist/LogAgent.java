package com.max.log.agent.javassist;

import com.max.log.agent.service.LogService;
import com.max.log.agent.util.ConfigUtil;

import java.lang.instrument.Instrumentation;
import java.util.Map;

public class LogAgent {

    public static void premain(String args, Instrumentation inst) {
        System.out.println("premain");
        Map<String, String> configMap = ConfigUtil.getConfig(args);
        if (null == configMap || configMap.size() == 0) {
            return;
        }
        LogService.createDataSource();
        customLogic(inst);
    }

    public static void agentmain(String args, Instrumentation inst) {
        System.out.println("agentmain");
        Map<String, String> configMap = ConfigUtil.getConfig(args);
        if (null == configMap || configMap.size() == 0) {
            return;
        }
        LogService.createDataSource();
        customLogic(inst);
    }

    private static void customLogic(Instrumentation inst) {
        inst.addTransformer(new CostTransformer());
    }
}
