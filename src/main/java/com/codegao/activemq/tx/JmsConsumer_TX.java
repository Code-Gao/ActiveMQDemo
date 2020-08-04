package com.codegao.activemq.tx;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * @author huanyu
 * @date 2020/7/28 16:21
 */

public class JmsConsumer_TX {


    public static final String ACTIVEMQ_URL = "tcp://127.0.0.1:61616";
    public static final String QUEUE_NAME = "queue01";


    public static void main(String[] args) throws JMSException, IOException {
        //1.创建连接工厂,按照给定的url地址，采用默认的用户名和密码
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2.通过连接工厂，获得connection俩姐
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        //3.创建会话session
        //两个参数，第一个叫事务/第二个叫签收
        Session session = connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);

        //4.创建目的地（具体是对立还是主题topic）
        Queue quene = session.createQueue(QUEUE_NAME);

        //5.创建消费者
        MessageConsumer messageConsumer = session.createConsumer(quene);
        //同步阻塞方式receive
        while (true){
            TextMessage textMessage = (TextMessage) messageConsumer.receive(3000L);
            if (null != textMessage){
                System.out.println("****消费者接收到消息："+textMessage.getText());
                textMessage.acknowledge();
            }else {
                break;
            }
        }
        messageConsumer.close();
        session.commit();
        session.close();
        connection.close();



    }
}
