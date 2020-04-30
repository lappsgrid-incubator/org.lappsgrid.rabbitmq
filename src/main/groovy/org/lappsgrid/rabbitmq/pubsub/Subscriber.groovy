package org.lappsgrid.rabbitmq.pubsub

import com.rabbitmq.client.AMQP
import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.Consumer
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import org.lappsgrid.rabbitmq.RabbitMQ

/**
 *
 */
class Subscriber {
    String exchange
    String queueName
    Connection connection
    Channel channel

    Subscriber(String exchange) {
        this(exchange, RabbitMQ.getHost())

    }

    Subscriber(String exchange, String host) {
        this.exchange = exchange
        ConnectionFactory factory = new ConnectionFactory();
        int slash = host.indexOf('/')
        if (slash < 0) {
            factory.setHost(host);
        }
        else {
            String addr = host.substring(0, slash)
            String vhost = host.substring(slash+1)
            factory.setHost(addr)
            factory.setVirtualHost(vhost)
        }
        factory.setUsername(RabbitMQ.getUsername())
        factory.setPassword(RabbitMQ.getPassword())

        connection = factory.newConnection();
        channel = connection.createChannel();

        channel.exchangeDeclare(exchange, "fanout");
        queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, exchange, "");
    }

    void register(Consumer consumer) {
        channel.basicConsume(queueName, true, consumer)
    }

    void register(Closure cl) {
        Consumer consumer = new DefaultConsumer(this.channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                cl(message)
            }
        }
        register(consumer)
    }

    void close() {
        if (channel.isOpen()) channel.close()
        if (connection.isOpen()) connection.close()
    }

}
