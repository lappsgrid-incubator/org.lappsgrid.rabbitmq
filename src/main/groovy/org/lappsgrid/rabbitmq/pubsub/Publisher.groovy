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
        this(exchange, RabbitMQ.getHost())
    }

    public Publisher(String exchange, String host) {
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
    }

    void publish(String message) {
        channel.basicPublish(exchange, '', null, message.bytes)
    }

    void close() {
        if (channel.isOpen()) channel.close();
        if (connection.isOpen()) connection.close();
    }

}
