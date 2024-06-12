package com.message.util;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.message.utils.SmsUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class MessageUtilTests {

    @Test
    public void smsSend() {
        String host = "https://gyytz.market.alicloudapi.com";
        String path = "/sms/smsBatchSend";
        String method = "POST";
        String appcode = "2ed84e909bee49d097b8d93bd91f79d9";
        String smsSignId = "2e65b1bb3d054466b82f0c9d125465e2";
        String templateId = "4d6e38512fd54f939a4e06ef25e96e35";

        Map<String, String> querys = new HashMap<String, String>();
        Map<String, String> bodys = new HashMap<String, String>();
        JSONObject obj = new JSONObject();
        obj.put("name","苏勇登");
        obj.put("code","RK20240612001");
        obj.put("url","www.baidu.com");
        obj.put("mobile","15858414795");
        JSONObject obj2 = new JSONObject();
        obj2.put("name","公孙离");
        obj2.put("code","RK20240612002");
        obj2.put("url","www.baidu.com");
        obj2.put("mobile","15858414795");
        JSONArray jsonArray = new JSONArray();
        jsonArray.add(obj);
        jsonArray.add(obj2);
        String body = JSONUtil.toJsonStr(jsonArray);
        bodys.put("content", body);

        SmsUtil.sendSms(host, path, method, appcode, smsSignId, templateId, querys, bodys);
    }
}
