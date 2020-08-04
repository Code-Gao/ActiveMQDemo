package com.codegao.activemq.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.IOException;

/**
 * @author huanyu
 * @date 2020/7/24 17:27
 */

public class JmsConsumer {

    public static final String ACTIVEMQ_URL = "tcp://127.0.0.1:61616";
    public static final String QUEUE_NAME = "queue01";


    public static void main(String[] args) throws JMSException, IOException {
        //1.创建连接工厂,按照给定的url地址，采用默认的用户名和密码
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory(ACTIVEMQ_URL);
        //2.通过连接工厂，获得connection连接
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();

        //3.创建会话session
        //两个参数，第一个叫事务/第二个叫签收
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        //4.创建目的地（具体是对立还是主题topic）
        Queue quene = session.createQueue(QUEUE_NAME);

        //5.创建消费者
        MessageConsumer messageConsumer = session.createConsumer(quene);
        //同步阻塞方式receive
        /*while (true){
            TextMessage textMessage = (TextMessage) messageConsumer.receive(4000L);
            if (null != textMessage){
                System.out.println("****消费者接收到消息："+textMessage.getText());
            }else {
                break;
            }
        }
        messageConsumer.close();
        session.close();
        connection.close();*/

        //通过监听的方式来消费消息
        messageConsumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if (null != message && message instanceof TextMessage){
                    TextMessage textMessage = (TextMessage) message;
                    try {
                        System.out.println("***消费者接收到消息："+textMessage.getText());
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        System.in.read();           //让进程不关闭
        messageConsumer.close();
        session.close();
        connection.close();

    /*
    * 1.先生产，只启动1号消费者。问题：1号消费者能消费消息吗？
    * YES
    *
    * 2.先生产，先启动1号消费者，再启动2号消费者，问题：2号消费者还能消费消息吗？
    * N
    *
    * 3.先启动2个消费者，再生产6条消息，请问，消费情况如何？
    * 轮询
    * */


    }




}
