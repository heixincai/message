package com.message.utils;

import org.apache.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;

public class SmsUtil {
    public static void main(String[] args) {
        String host = "https://gyytz.market.alicloudapi.com";
        String path = "/sms/smsBatchSend";
        String method = "POST";
        String appcode = "2ed84e909bee49d097b8d93bd91f79d9";
        Map<String, String> headers = new HashMap<String, String>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
        bodys.put("content", "[{\"mobile\":\"15858414795\",\"**code**\":\"123456\",\"**minute**\":\"5\"},{\"mobile\":\"15858414795\",\"**code**\":\"56789\",\"**minute**\":\"10\"}]");

//smsSignId（短信前缀）和templateId（短信模板），可登录国阳云控制台自助申请。参考文档：http://help.guoyangyun.com/Problem/Qm.html

        bodys.put("smsSignId", "2e65b1bb3d054466b82f0c9d125465e2");
        bodys.put("templateId", "908e94ccf08b4476ba6c876d13f084ad");


        try {
            /**
             * 重要提示如下:
             * HttpUtils请从\r\n\t    \t* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java\r\n\t    \t* 下载
             *
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
            //String response = HttpRequest.post(host + path).header("Authorization", "APPCODE" + appcode).body(JSONUtil.toJsonStr(bodys)).execute().body();
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            System.out.println(response.toString());
            //获取response的body
            //System.out.println(EntityUtils.toString(response.getEntity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}