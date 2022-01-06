package com.max.log.agent.constant;

public class Constant {

    public static final String APP = "app";//应用名称
    public static final String EXCLUDE = "exclude";//不需要日志打印的包
    public static final String INCLUDE = "include";//需要日志打印的包
    public static final String SEND = "send";//日志是否需要发送，值可以是es，kafka，mongo,none
    public static final String URL = "url";//es或kafka访问地址
    public static final String ASYNC = "async";//日志是否异步发送，值可以是true，false,参考rocketmq刷盘方式
    public static final String BATCH = "batch";//日志发送批量大小，值可以是5，表示每达到5个就发送，参考redis rdb持久化配置
    public static final String ITEM_SPLIT = ",";
    public static final String ITEM_VALUE_SPLIT = "=";

    public static final Integer DEFAULT_BATCH_SIZE = 10;

    public static final String ES_CREATE_INDEX = "{\"settings\":{\"number_of_shards\":3,\"number_of_replicas\":1},\"mappings\":{\"log\":{\"properties\":{\"traceId\":{\"type\":\"keyword\"},\"clazz\":{\"type\":\"text\"},\"method\":{\"type\":\"text\"},\"param\":{\"type\":\"keyword\"},\"time\":{\"type\":\"integer\"},\"timestmap\":{\"type\":\"date\",\"format\":\"yyyy-MM-dd HH:mm:ss || yyyy-MM-dd || epoch_millis\"}}}}}";
}
