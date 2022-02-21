package com.atguigu.srb.sms.service;

import java.util.Map;

/**
 * @Author wangjun
 * @Date 2021/9/16 16:21
 * @Description
 */
public interface SmsService {
    void send(String mobile, String templateCode, Map<String,Object> param);
}
