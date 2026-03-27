package com.example.intelligentfarmcore.service;

import com.example.intelligentfarmcore.config.AmqpProperties;
import com.example.intelligentfarmcore.pojo.entity.EnvironmentData;
import com.example.intelligentfarmcore.service.interfaces.IDeviceService;
import com.example.intelligentfarmcore.service.interfaces.IEnvironmentSensorService;
import com.example.intelligentfarmcore.utils.SensorMessageUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.qpid.jms.JmsConnection;
import org.apache.qpid.jms.JmsConnectionListener;
import org.apache.qpid.jms.message.JmsInboundMessageDispatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.net.URI;
import java.util.Hashtable;
import java.util.UUID;

@Service
public class AmqpConsumerService implements InitializingBean, DisposableBean {
    private static final Logger logger = LoggerFactory.getLogger(AmqpConsumerService.class);
    @Autowired
    IEnvironmentSensorService environmentSensorService;
    @Autowired
    IDeviceService deviceService;
    @Autowired
    private AmqpProperties amqpProperties;

    private Connection connection;
    private Session session;
    private MessageConsumer consumer;

    @Override
    public void afterPropertiesSet() throws Exception {
        subscribe();
    }

    @Override
    public void destroy() throws Exception {
        close();
    }

    private void subscribe() throws Exception {
        String accessKey = amqpProperties.getAccessKey();
        String accessSecret = amqpProperties.getAccessSecret();
        String consumerGroupId = amqpProperties.getConsumerGroupId();
        String host = amqpProperties.getHost();
        int port = amqpProperties.getPort();
        String signMethod = amqpProperties.getSignMethod();
        String iotInstanceId = amqpProperties.getIotInstanceId();

        long timeStamp = System.currentTimeMillis();
        String clientId = UUID.randomUUID().toString().replaceAll("-", "");
        System.out.println(
                accessKey + " " + accessSecret + " " + consumerGroupId + " " + host + " " + port + " " + signMethod);
        String userName = clientId + "|authMode=aksign"
                + ",signMethod=" + signMethod
                + ",timestamp=" + timeStamp
                + ",authId=" + accessKey
                + ",iotInstanceId=" + iotInstanceId // 添加这个参数
                + ",consumerGroupId=" + consumerGroupId
                + "|";

        String signContent = "authId=" + accessKey + "&timestamp=" + timeStamp;
        String password = doSign(signContent, accessSecret, signMethod);

        String connectionUrl = "failover:(amqps://" + host + ":5671?amqp.idleTimeout=80000)"
                + "?failover.reconnectDelay=30";

        Hashtable<String, String> hashtable = new Hashtable<>();
        hashtable.put("connectionfactory.SBCF", connectionUrl);
        hashtable.put("queue.QUEUE", "default");
        hashtable.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.qpid.jms.jndi.JmsInitialContextFactory");
        Context context = new InitialContext(hashtable);

        ConnectionFactory cf = (ConnectionFactory) context.lookup("SBCF");
        Destination queue = (Destination) context.lookup("QUEUE");

        connection = cf.createConnection(userName, password);
        ((JmsConnection) connection).addConnectionListener(new JmsConnectionListener() {
            @Override
            public void onConnectionEstablished(URI remoteURI) {
                logger.info("AMQP连接已建立: {}", remoteURI);
            }

            @Override
            public void onConnectionFailure(Throwable error) {
                logger.error("AMQP连接失败: {}", error.getMessage(), error);
            }

            @Override
            public void onConnectionInterrupted(URI remoteURI) {
                logger.warn("AMQP连接中断: {}", remoteURI);
            }

            @Override
            public void onConnectionRestored(URI remoteURI) {
                logger.info("AMQP连接已恢复: {}", remoteURI);
            }

            @Override
            public void onInboundMessage(JmsInboundMessageDispatch messageDispatch) {
                // 可以留空或添加处理逻辑
            }

            @Override
            public void onSessionClosed(Session session, Throwable throwable) {
                logger.error("AMQP会话关闭: {}", throwable.getMessage());
            }

            @Override
            public void onConsumerClosed(MessageConsumer messageConsumer, Throwable throwable) {

            }

            @Override
            public void onProducerClosed(MessageProducer messageProducer, Throwable throwable) {

            }
        });

        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        connection.start();

        consumer = session.createConsumer(queue);
        consumer.setMessageListener(message -> {
            try {
                byte[] body = message.getBody(byte[].class);
                if (body == null) {
                    logger.warn("收到空消息，topic: {}", message.getStringProperty("topic"));
                    return;
                }
                String payload = new String(body);
                String topic = message.getStringProperty("topic");
                logger.info("收到消息: topic={}, payload={}", topic, payload);
                // 处理接收到的消息

                // 解析环境数据
                EnvironmentData environmentData = SensorMessageUtils.parseSensorMessage(payload);
                if (environmentData != null) {
                    // 添加设备数据
                    Long deviceId = deviceService.updateDeviceData(environmentData.toDevice());
                    // 保存到数据库
                    environmentData.setDeviceId(deviceId);
                    environmentSensorService.addEnvironmentSensor(environmentData);
                }
            } catch (Exception e) {
                logger.error("处理消息异常", e);
            }
        });
    }

    private void close() {
        try {
            if (consumer != null)
                consumer.close();
            if (session != null)
                session.close();
            if (connection != null)
                connection.close();
            logger.info("AMQP消费者已关闭");
        } catch (Exception e) {
            logger.error("关闭AMQP连接时出错", e);
        }
    }

    private String doSign(String toSignString, String secret, String signMethod) throws Exception {
        SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(), signMethod);
        Mac mac = Mac.getInstance(signMethod);
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(toSignString.getBytes());
        return Base64.encodeBase64String(rawHmac);
    }
}
