package com.key.win.common.remote;


import com.key.win.common.remote.enums.ResponseCodeEnum;
import com.key.win.common.remote.vo.ResponseVo;
import com.key.win.common.util.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class BaseRemoteService {

    private final static Logger logger = LoggerFactory.getLogger(BaseRemoteService.class);

    private static final String POST = "POST";
    private static final String UTF_8 = "UTF-8";

    public static StringBuffer postMessage(Object t, String urlString) throws Exception {
        return postMessage(t,urlString,new HashMap<>());
    }

    public static StringBuffer postMessage(Object t, String urlString, Map<String, String> map) throws Exception {
        StringBuffer sTotalString = getReturnJson(t, urlString, map);
        return sTotalString;
    }

    public static StringBuffer postMessage(Object t, String urlString, String token) throws Exception {
        Map<String, String> map = getTokenToMap(token);
        StringBuffer sTotalString = getReturnJson(t, urlString, map);
        return sTotalString;
    }

    public static ResponseVo postMessage(Object t, String urlString, Class<?>... subClass) throws Exception {
        return postMessage(t,urlString,new HashMap<>(),subClass);
    }

    public static ResponseVo postMessage(Object t, String urlString, String token,Class<?>... subClass) throws Exception {
        Map<String, String> map = getTokenToMap(token);
        return postMessage(t,urlString,map,subClass);
    }

    public static ResponseVo postMessage(Object t, String urlString, Map<String, String> map, Class<?>... subClass) throws Exception {
        StringBuffer sTotalString = getReturnJson(t, urlString, map);
        ResponseVo response = (ResponseVo) JsonUtils.toObject(sTotalString.toString(), ResponseVo.class, subClass);
        if (ResponseCodeEnum.SUCCESS.getCode().equals(response.getResponseCode())) {
            logger.error("[" + urlString + "]接口调用成功！");
        } else {
            logger.error("[" + urlString + "]接口调用失败->返回结果出错：code=" + response.getResponseCode() + "error:" + response.getResponseMessage());
        }
        return response;
    }



    public static StringBuffer getReturnJson(Object t, String urlString, Map<String, String> map) throws Exception {
        String jsonStr = JsonUtils.toJsonNoException(t);
        logger.info("上报数据json:" + jsonStr);
        SSLContext sslcontext = SSLContext.getInstance("SSL", "SunJSSE");
        sslcontext.init(null, new TrustManager[]{new MyX509TrustManager()}, new java.security.SecureRandom());
        HostnameVerifier ignoreHostnameVerifier = new HostnameVerifier() {
            public boolean verify(String s, SSLSession sslsession) {
                System.out.println("WARNING: Hostname is not matched for cert.");
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(ignoreHostnameVerifier);
        HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        connection.setRequestProperty("charsert", "utf-8");
        connection.setRequestProperty("Content-Type", "application/json");
        setConnectionHeader(map, connection);
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.setDoOutput(true);
        connection.setRequestMethod(BaseRemoteService.POST);
        //	OutputStream out = connection.getOutputStream();
        OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "utf-8");
        if (StringUtils.isNotEmpty(jsonStr)) {
            //out.write(jsonStr.getBytes(BaseRemoteService.UTF_8));
            out.write(jsonStr);
        }
        out.flush();
        out.close();
        // 获取响应代码
        int code = connection.getResponseCode();
        // 获取页面内容
        InputStream in = connection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, Charset.forName(BaseRemoteService.UTF_8)));
        StringBuffer sTotalString = new StringBuffer();
        String sCurrentLine = "";
        while ((sCurrentLine = bufferedReader.readLine()) != null) {
            sTotalString.append(sCurrentLine);
        }
        bufferedReader.close();
        connection.disconnect(); // 关闭http连接
        return sTotalString;
    }

    private static void setConnectionHeader(Map<String, String> map, HttpURLConnection connection) {
        if (map != null && map.size() > 0) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String mapKey = entry.getKey();
                String mapValue = entry.getValue();
                connection.setRequestProperty(mapKey, mapValue);
            }
        }
    }

    public static StringBuffer getJsonStringByConnection(String urlString) throws Exception {
        return getJsonStringByConnection(urlString,new HashMap<>());
    }

    public static StringBuffer getJsonStringByConnection(String urlString,Map<String, String> map) throws Exception {
        StringBuffer sTotalString = getReturnJson(urlString,map);
        return sTotalString;
    }
    public static StringBuffer getJsonStringByConnection(String urlString,String token) throws Exception {
        Map<String, String> map = getTokenToMap(token);
        StringBuffer sTotalString = getReturnJson(urlString,map);
        return sTotalString;
    }

    public static StringBuffer getReturnJson(String urlString, Map<String, String> map) throws Exception {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        InputStream urlStream = connection.getInputStream();
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        setConnectionHeader(map, connection);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlStream,
                Charset.forName(BaseRemoteService.UTF_8)));
        StringBuffer sTotalString = new StringBuffer();
        String sCurrentLine = "";
        while ((sCurrentLine = bufferedReader.readLine()) != null) {
            sTotalString.append(sCurrentLine);
        }
        bufferedReader.close();
        connection.disconnect();
        return sTotalString;
    }

    public static ResponseVo getJsonStringByConnection(String urlString, Class<?>... subClass) throws Exception {
        return getJsonStringByConnection(urlString,new HashMap<>(),subClass);
    }
    public static ResponseVo getJsonStringByConnection(String urlString,String token, Class<?>... subClass) throws Exception {
        Map<String, String> map = getTokenToMap(token);
        return getJsonStringByConnection(urlString,map,subClass);
    }

    private static Map<String, String> getTokenToMap(String token) {
        Map<String, String> map = new HashMap<>();
        map.put("Authorization", "Bearer " + token);
        return map;
    }

    public static ResponseVo getJsonStringByConnection(String urlString,Map<String, String> map, Class<?>... subClass) throws Exception {
        StringBuffer sTotalString = getReturnJson(urlString,map);
        ResponseVo response = JsonUtils.toObject(sTotalString.toString(), ResponseVo.class, subClass);
        if (ResponseCodeEnum.SUCCESS.getCode().equals(response.getResponseCode())) {
            logger.info("[" + urlString + "]接口调用成功！");
        } else {
            logger.error("[" + urlString + "]接口调用失败->返回结果出错：code=" + response.getResponseCode() + "error:" + response.getResponseMessage());
        }
        return response;
    }


}
