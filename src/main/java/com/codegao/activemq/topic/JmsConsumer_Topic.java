package com.codegao.activemq.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * @author huanyu
 * @date 2020/7/27 16:14
 */

public class JmsConsumer_Topic {
    public static final String ACTIVEMQ_URL = "tcp://127.0.0.1:61616";
    public static final String TOPIC_NAME = "topic-atguigu";


    public static void main(String[] args) throws JMSException, IOException {
        System.out.println("***我是二号消费者");
        //1.创建连接工厂,按照给定的url地址，采用默认的用户名和密码
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2.通过连接工厂，获得connection俩姐
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        //3.创建会话session
        //两个参数，第一个叫事务/第二个叫签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //4.创建目的地（具体是对立还是主题topic）
        Topic topic = session.createTopic(TOPIC_NAME);

        //5.创建消费者
        MessageConsumer messageConsumer = session.createConsumer(topic);


        //通过监听的方式来消费消息

        messageConsumer.setMessageListener((Message message) ->{
            if (null != message && message instanceof TextMessage){
                TextMessage textMessage = (TextMessage) message;
                try {
                    System.out.println("***消费者接收到Topic消息："+textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        System.in.read();           //让进程不关闭
        messageConsumer.close();
        session.close();
        connection.close();




    }
}
