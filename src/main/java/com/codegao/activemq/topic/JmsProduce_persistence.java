package com.codegao.activemq.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

/**
 * @author huanyu
 * @date 2020/7/28 17:45
 */

public class JmsProduce_persistence {
    public static final String ACTIVEMQ_URL = "tcp://127.0.0.1:61616";
    public static final String TOPIC_NAME = "MQ-topic-persistence";


    public static void main(String[] args) throws JMSException {
        //1.创建连接工厂,按照给定的url地址，采用默认的用户名和密码
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2.通过连接工厂，获得connection连接
        Connection connection = activeMQConnectionFactory.createConnection();

        //3.创建会话session
        //两个参数，第一个叫事务/第二个叫签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //4.创建目的地（具体是对立还是主题topic）
        Topic topic = session.createTopic(TOPIC_NAME);

        //5.创建消息的生产者
        MessageProducer messageProducer = session.createProducer(topic);

        //设置持久化topic
        messageProducer.setDeliveryMode(DeliveryMode.PERSISTENT);
        connection.start();
        //通过使用messageProducer
        for(int i = 1; i<=4;i++){
            //7.创建消息
            TextMessage textMessage = session.createTextMessage("TOPIC_NAME---"+i);

            //8.通过messageProducer发送给mq
            messageProducer.send(textMessage);
        }
        //9.关闭资源
        messageProducer.close();
        session.close();
        connection.close();
        System.out.println("****TOPIC_NAME消息发送到MQ完成");

    }
}
