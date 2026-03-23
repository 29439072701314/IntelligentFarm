package com.example.intelligentfarmcore.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "aliyun.amqp")
public class AmqpProperties {
    private String accessKey;
    private String accessSecret;
    private String consumerGroupId;
    private String host;
    private int port;
    private String signMethod = "hmacsha1";
    private String iotInstanceId;
    // getters and setters
    public String getAccessKey() { return accessKey; }
    public void setAccessKey(String accessKey) { this.accessKey = accessKey; }
    public String getAccessSecret() { return accessSecret; }
    public void setAccessSecret(String accessSecret) { this.accessSecret = accessSecret; }
    public String getConsumerGroupId() { return consumerGroupId; }
    public void setConsumerGroupId(String consumerGroupId) { this.consumerGroupId = consumerGroupId; }
    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }
    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }
    public String getSignMethod() { return signMethod; }
    public void setSignMethod(String signMethod) { this.signMethod = signMethod; }
    public String getIotInstanceId() { return iotInstanceId; }
    public void setIotInstanceId(String iotInstanceId) { this.iotInstanceId = iotInstanceId; }
}