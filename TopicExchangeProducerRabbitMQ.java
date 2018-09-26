import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class TopicExchangeProducerRabbitMQ {
    // exchangeName
    public static String EXCHANGE_NAME = "topicExchange";

    public static void main(String[] args) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = null;
        Channel channel = null;
        try {
            // 1.Create connections and channels
            connection = factory.newConnection();
            channel = connection.createChannel();

            // 2.Declaring the topic type of exchange for the channel
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
            
            // 3.Send the message to the specified exchange, the queue is specified empty, and exchange determines which queue to send according to the situation.
         //   String routingKey = "info";
           String routingKey = "log4j.error";
//            String routingKey = "logback.error";
//            String routingKey = "log4j.warn";
            String msg = " hello rabbitmq, I am " + routingKey;
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, msg.getBytes());
            System.out.println("product send a msg: " + msg);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            // 4.Close connection

            if (connection != null) {
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}