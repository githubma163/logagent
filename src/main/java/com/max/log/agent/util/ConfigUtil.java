package com.max.log.agent.util;

import com.max.log.agent.constant.Constant;

import java.util.HashMap;
import java.util.Map;

//解析配置文件
public class ConfigUtil {

    public static Map<String, String> CONFIG_MAP = new HashMap<String, String>();

    public static Map<String, String> getConfig(String args) {
        System.out.println("log agent init config:" + args);
        Map<String, String> map = new HashMap<String, String>();
        if (StringUtils.isBlank(args)) {
            return map;
        }
        String[] configs = args.split(Constant.ITEM_SPLIT);
        for (String item : configs) {
            String[] itemKeyValue = item.split(Constant.ITEM_VALUE_SPLIT);
            if (null != itemKeyValue && itemKeyValue.length == 2) {
                if (map.containsKey(itemKeyValue[0])) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(map.get(itemKeyValue[0]));
                    sb.append(Constant.ITEM_SPLIT);
                    sb.append(itemKeyValue[1]);
                    map.put(itemKeyValue[0], sb.toString());
                } else {
                    map.put(itemKeyValue[0], itemKeyValue[1]);
                }
            }
        }
        CONFIG_MAP = map;
        System.out.println("log agent init config parse result:" + map);
        return map;
    }

}
