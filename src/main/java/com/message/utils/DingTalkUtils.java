package com.message.utils;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.TimedCache;
import cn.hutool.json.JSONUtil;
import com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenResponse;
import com.aliyun.dingtalkrobot_1_0.models.BatchSendOTOHeaders;
import com.aliyun.dingtalkrobot_1_0.models.BatchSendOTORequest;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiV2UserGetbymobileRequest;
import com.dingtalk.api.response.OapiV2UserGetbymobileResponse;
import org.apache.commons.lang.StringUtils;

public class DingTalkUtils {

    //钉钉的Token是2小时过期，这里缓存设置小于2小时
    private static final TimedCache<String, String> ACCESS_TOKEN_CACHE = CacheUtil.newTimedCache(7000000);
    private static final String ACCESS_TOKEN = "accessToken";

    /**
     * 使用 Token 初始化账号Client
     * @return Client
     * @throws Exception
     */
    public static com.aliyun.dingtalkoauth2_1_0.Client createClient() throws Exception {
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config();
        config.protocol = "https";
        config.regionId = "central";
        return new com.aliyun.dingtalkoauth2_1_0.Client(config);
    }

    public static com.aliyun.dingtalkrobot_1_0.Client create1Client() throws Exception {
        Config config = new Config();
        config.protocol = "https";
        config.regionId = "central";
        return new com.aliyun.dingtalkrobot_1_0.Client(config);
    }

    /**
     * 获取token：appKey、appSecret来自于钉钉内创建的小机器人，获取方式参考下方链接
     * 代码来自于官方文档：https://open.dingtalk.com/document/isvapp/robot-reply-send-message
     * @param appKey
     * @param appSecret
     * @return
     * @throws Exception
     */
    public static String getAccessToken(String appKey, String appSecret) throws Exception {
        String token = ACCESS_TOKEN_CACHE.get(ACCESS_TOKEN);
        if (StringUtils.isNotEmpty(token)) {
            return token;
        }
        com.aliyun.dingtalkoauth2_1_0.Client client = createClient();
        com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenRequest getAccessTokenRequest = new com.aliyun.dingtalkoauth2_1_0.models.GetAccessTokenRequest()
                .setAppKey(appKey)
                .setAppSecret(appSecret);
        try {
            GetAccessTokenResponse response = client.getAccessToken(getAccessTokenRequest);
            token = response.getBody().getAccessToken();
        } catch (TeaException err) {
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }
        } catch (Exception _err) {
            TeaException err = new TeaException(_err.getMessage(), _err);
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }
        }
        ACCESS_TOKEN_CACHE.put(ACCESS_TOKEN, token);
        return token;
    }

    /**
     * 根据手机号获取用户信息，参考官方文档：https://open.dingtalk.com/document/orgapp/query-users-by-phone-number
     * @param mobile
     * @param appKey
     * @param appSecret
     * @return
     */
    public static String getUserInfoByMobile(String mobile, String appKey, String appSecret) {
        String userId = null;
        try {
            DingTalkClient client = new DefaultDingTalkClient("https://oapi.dingtalk.com/topapi/v2/user/getbymobile");
            OapiV2UserGetbymobileRequest req = new OapiV2UserGetbymobileRequest();
            req.setMobile(mobile);
            //获取token
            String token = getAccessToken(appKey, appSecret);
            OapiV2UserGetbymobileResponse rsp = client.execute(req, token);
            if (rsp.isSuccess()) {
                String body = rsp.getBody();
                if (StringUtils.isNotBlank(body)) {
                    userId = JSONUtil.parseObj(body).getJSONObject("result").getStr("userid");
                }
            }
            System.out.println(rsp.getBody());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userId;
    }

    /**
     * 批量发送单聊消息，官方文档：https://open.dingtalk.com/document/robots/chatbots-send-one-on-one-chat-messages-in-batches
     * @param robotCode
     * @param mobile
     * @param appKey
     * @param appSecret
     * @param msgKey
     * @param content
     * @throws Exception
     */
    public static void sendMessage(String robotCode, String mobile, String appKey, String appSecret, String msgKey, String content) throws Exception {
        //获取Token
        String accessToken = getAccessToken(appKey, appSecret);
        //根据手机号获取用户详情
        String userId = getUserInfoByMobile(mobile, appKey, appSecret);
        com.aliyun.dingtalkrobot_1_0.Client client = create1Client();
        BatchSendOTOHeaders batchSendOTOHeaders = new BatchSendOTOHeaders();
        batchSendOTOHeaders.xAcsDingtalkAccessToken = accessToken;
        BatchSendOTORequest batchSendOTORequest = new BatchSendOTORequest()
                .setRobotCode(robotCode)
                .setUserIds(java.util.Arrays.asList(
                        userId
                ))
                .setMsgKey(msgKey)
                .setMsgParam(content);
        try {
            client.batchSendOTOWithOptions(batchSendOTORequest, batchSendOTOHeaders, new RuntimeOptions());
        } catch (TeaException err) {
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }

        } catch (Exception _err) {
            TeaException err = new TeaException(_err.getMessage(), _err);
            if (!com.aliyun.teautil.Common.empty(err.code) && !com.aliyun.teautil.Common.empty(err.message)) {
                // err 中含有 code 和 message 属性，可帮助开发定位问题
            }
        }
    }

}
