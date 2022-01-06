package com.max.log.agent.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

//发送http请求
public class HttpUtil {

    private static final String ACCEPT = "*/*";
    private static final String CONNECTION = "Keep-Alive";
    private static final String USER_AGENT = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)";

    public static String sendPost(String url, String content) {
        URL realUrl = null;
        BufferedReader in = null;
        InputStream inputStream = null;
        String result = "";
        try {
            realUrl = new URL(url);
            HttpURLConnection httpCon = (HttpURLConnection) realUrl.openConnection();
            httpCon.setRequestProperty("Accept", ACCEPT);
            httpCon.setRequestProperty("Connection", CONNECTION);
            httpCon.setRequestProperty("User-Agent", USER_AGENT);
            httpCon.setRequestProperty("Content-Type", "application/json; utf-8");
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("POST");
            OutputStreamWriter out = new OutputStreamWriter(
                    httpCon.getOutputStream());
            out.write(content);
            out.close();
            inputStream = httpCon.getInputStream();
            in = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            //inputStream.close();
            //in.close();
        } catch (Exception e) {
            System.out.println(e.getMessage() + "," + content);
        }
        return result;
    }

    public static String sendPut(String url, String content) {
        URL realUrl = null;
        BufferedReader in = null;
        String result = "";
        try {
            realUrl = new URL(url);
            HttpURLConnection httpCon = (HttpURLConnection) realUrl.openConnection();
            httpCon.setRequestProperty("Accept", ACCEPT);
            httpCon.setRequestProperty("Connection", CONNECTION);
            httpCon.setRequestProperty("User-Agent", USER_AGENT);
            httpCon.setRequestProperty("Content-Type", "application/json; utf-8");
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("PUT");
            OutputStreamWriter out = new OutputStreamWriter(
                    httpCon.getOutputStream());
            out.write(content);
            out.close();
            in = new BufferedReader(new InputStreamReader(httpCon.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static int sendHead(String url) {
        URL realUrl = null;
        BufferedReader in = null;
        String result = "";
        int statusCode = 0;
        try {
            realUrl = new URL(url);
            HttpURLConnection httpCon = (HttpURLConnection) realUrl.openConnection();
            httpCon.setRequestProperty("Accept", ACCEPT);
            httpCon.setRequestProperty("Connection", CONNECTION);
            httpCon.setRequestProperty("User-Agent", USER_AGENT);
            httpCon.setDoOutput(true);
            httpCon.setRequestMethod("HEAD");
            OutputStreamWriter out = new OutputStreamWriter(
                    httpCon.getOutputStream());
            statusCode = httpCon.getResponseCode();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusCode;
    }
}
