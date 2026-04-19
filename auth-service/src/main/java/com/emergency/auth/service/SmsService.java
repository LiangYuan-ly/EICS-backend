package com.emergency.auth.service;

import com.emergency.auth.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class SmsService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String SMS_CODE_PREFIX = "sms:code:";
    private static final long SMS_CODE_EXPIRE = 5; // 5分钟

    public Result<String> sendSmsCode(String adminPhone) {
        // 生成6位随机验证码
        String code = generateRandomCode(6);

        // 保存验证码到Redis，设置5分钟异常
        stringRedisTemplate.opsForValue().set(SMS_CODE_PREFIX + adminPhone, code, SMS_CODE_EXPIRE, TimeUnit.MINUTES);

        // 模拟发送真实短信
        /*
         * // 暂时不准备发送真实的短信验证码
         * // 以后需要时可以接入阿里云或者腾讯云的短信服务
         * // sendRealSms(adminPhone, code);
         */

        // 仅供开发调试：打印验证码
        System.out.println("向手机号 " + adminPhone + " 发送短信验证码: " + code);

        return Result.success("验证码发送成功");
    }

    private String generateRandomCode(int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
