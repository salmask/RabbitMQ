import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class TopicExchangeConsumerRabbitMQ {

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
            channel.exchangeDeclare(TopicExchangeProducerRabbitMQ .EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
            // 3.Create a queue of random names
            String queueName = channel.queueDeclare().getQueue();

            // 4.Setting up the binding relationship between exchange and queue
        //    String[] bindingKeys = { "#" };
         String[] bindingKeys = { "log4j.*", "#.error" };
//            String[] bindingKeys = { "*.error" };
//            String[] bindingKeys = { "log4j.warn" };
            for (int i = 0; i < bindingKeys.length; i++) {
                channel.queueBind(queueName, TopicExchangeProducerRabbitMQ.EXCHANGE_NAME, bindingKeys[i]);
                System.out.println(" **** LogTopicReciver keep alive ,waiting for " + bindingKeys[i]);
            }

            // 5.Generating consumers and listening through callbacks
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                        com.rabbitmq.client.AMQP.BasicProperties properties, byte[] body) throws IOException {

                    // Get the content of the message and handle it
                    String msg = new String(body, "UTF-8");
                    System.out.println("*********** LogTopicReciver" + " get message :[" + msg + "]");
                }
            };
            // 6.Consumer News
            channel.basicConsume(queueName, true, consumer);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
 