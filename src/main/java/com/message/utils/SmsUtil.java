package com.message.utils;

import org.apache.http.HttpResponse;

import java.util.HashMap;
import java.util.Map;

public class SmsUtil {

    /**
     *
     * @param host 短信服务商地址，目前使用的是：https://gyytz.market.alicloudapi.com
     * @param path 发送短信的路径
     * @param method GET、POST、等
     * @param appCode 短信服务商提供
     * @param smsSignId 签名模板ID、短信服务商提供 可登录国阳云控制台自助申请。参考文档：http://help.guoyangyun.com/Problem/Qm.html
     * @param templateId 短信内容模板ID、短信服务商提供 可登录国阳云控制台自助申请。参考文档：http://help.guoyangyun.com/Problem/Qm.html
     * @param querys 查询参数，主要用在GET请求，拼接在URL后
     * @param bodys 短信内容
     */
    public static HttpResponse sendSms(String host, String path, String method, String appCode, String smsSignId, String templateId, Map<String, String> querys, Map<String, String> bodys) {
        HttpResponse response = null;
        try {
            /**
             * 重要提示如下:
             * HttpUtils请从\r\n\t    \t* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java\r\n\t    \t* 下载
             * 相应的依赖请参照
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */

            /**
             * 最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
             * 根据API的要求，定义相对应的Content-Type
             */
            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "APPCODE " + appCode);
            headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

            /**
             * smsSignId（短信前缀）和templateId（短信模板），可登录国阳云控制台自助申请。参考文档：http://help.guoyangyun.com/Problem/Qm.html
             */
            bodys.put("smsSignId", smsSignId);
            bodys.put("templateId", templateId);

            response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

}
