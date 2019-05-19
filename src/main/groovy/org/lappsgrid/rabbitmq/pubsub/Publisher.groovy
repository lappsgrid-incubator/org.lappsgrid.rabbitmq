package org.lappsgrid.rabbitmq.pubsub


import com.rabbitmq.client.Channel
import com.rabbitmq.client.Connection
import com.rabbitmq.client.ConnectionFactory
import org.lappsgrid.rabbitmq.RabbitMQ


/**
 *
 */
class Publisher {
    String exchange
    Connection connection
    Channel channel

    public Publisher(String exchange) {
        this(exchange, RabbitMQ.DEFAULT_HOST)
    }

    public Publisher(String exchange, String host) {
        this.exchange = exchange
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);
        factory.setUsername('eager')
        factory.setPassword('eager')

        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.exchangeDeclare(exchange, "fanout");
    }

    void publish(String message) {
        channel.basicPublish(exchange, '', null, message.bytes)
    }

    void close() {
        if (channel.isOpen()) channel.close();
        if (connection.isOpen()) connection.close();
    }

}
