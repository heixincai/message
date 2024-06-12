package com.message.utils;

import cn.hutool.extra.mail.MailUtil;

public class MailUtils {

    public static void main(String[] args) {
        MailUtil.send("suyd@mingdutech.com", "测试", "邮件来自Hutool测试", false);
    }

}
