package com.max.log.agent;

public class Log {

    private String traceId;
    private String clazz;
    private String method;
    private String param;
    private Long time;
    private String msg;
    private String timestmap;

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTimestmap() {
        return timestmap;
    }

    public void setTimestmap(String timestmap) {
        this.timestmap = timestmap;
    }

    @Override
    public String toString() {
        return "{\"traceId\":\"" + traceId + "\",\"clazz\":\"" + clazz + "\",\"method\":\"" + method + "\",\"param\":\"" + param + "\",\"time\":" + time + ",\"timestmap\":\"" + timestmap + "\"}";
    }
}
