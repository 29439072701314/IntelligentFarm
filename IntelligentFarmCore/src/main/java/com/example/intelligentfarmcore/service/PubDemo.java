package com.example.intelligentfarmcore.service;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import com.aliyuncs.iot.model.v20180120.*;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import io.github.cdimascio.dotenv.Dotenv;

public class PubDemo {

    public static void main(String[] args) throws Exception {
        // 加载.env文件
        Dotenv dotenv = Dotenv.load();

        // 从环境变量或.env文件读取配置
        String accessKeyId = System.getenv("ALIYUN_ACCESS_KEY_ID");
        String accessKeySecret = System.getenv("ALIYUN_ACCESS_KEY_SECRET");

        // 如果环境变量未设置，从.env文件读取
        if (accessKeyId == null || accessKeyId.isEmpty()) {
            accessKeyId = dotenv.get("ALIYUN_ACCESS_KEY_ID", "YOUR_ACCESS_KEY_ID");
        }
        if (accessKeySecret == null || accessKeySecret.isEmpty()) {
            accessKeySecret = dotenv.get("ALIYUN_ACCESS_KEY_SECRET", "YOUR_ACCESS_KEY_SECRET");
        }

        DefaultProfile profile = DefaultProfile.getProfile("cn-shanghai", accessKeyId, accessKeySecret);

        IAcsClient client = new DefaultAcsClient(profile);

        PubRequest request = new PubRequest();
        request.setIotInstanceId("iot-06z00hax6b0i8g0");
        request.setProductKey("k1x0t4xETH9");
        request.setTopicFullName("/k1x0t4xETH9/Environmental/user/get");
        String originalText = "isFallDown=0";
        byte[] textBytes = originalText.getBytes(StandardCharsets.UTF_8);
        String base64Encoded = Base64.getEncoder().encodeToString(textBytes);
        request.setMessageContent(base64Encoded);

        try {
            PubResponse response = client.getAcsResponse(request);
            System.out.println(new Gson().toJson(response));
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            System.out.println("ErrCode:" + e.getErrCode());
            System.out.println("ErrMsg:" + e.getErrMsg());
            System.out.println("RequestId:" + e.getRequestId());
        }
    }
}