package com.codegao.activemq.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * @author huanyu
 * @date 2020/7/28 17:48
 */

public class JmsConsumer_persistence {
    public static final String ACTIVEMQ_URL = "tcp://127.0.0.1:61616";
    public static final String TOPIC_NAME = "MQ-topic-persistence";


    public static void main(String[] args) throws JMSException, IOException {
        System.out.println("***我是一号消费者");
        //1.创建连接工厂,按照给定的url地址，采用默认的用户名和密码
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2.通过连接工厂，获得connection连接
        Connection connection = activeMQConnectionFactory.createConnection();

        //设置客户端ID,向MQ服务器注册自己的名称
        connection.setClientID("merry");

        //3.创建会话session
        //两个参数，第一个叫事务/第二个叫签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //4.创建目的地（具体是对立还是主题topic）
        Topic topic = session.createTopic(TOPIC_NAME);

        //创建一个topic订阅者对象。一参是topic，二参事订阅者名称
        TopicSubscriber topicSubscriber = session.createDurableSubscriber(topic,"remark...");

        connection.start();

        Message message = topicSubscriber.receive();
        while (null != message){
            TextMessage textMessage = (TextMessage) message;
            System.out.println("收到的持久化 topic："+textMessage.getText());
            message = topicSubscriber.receive();
        }

        session.close();
        connection.close();

    }
}
