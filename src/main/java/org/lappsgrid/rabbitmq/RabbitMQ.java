package org.lappsgrid.rabbitmq;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *
 */
public class RabbitMQ {
    public static final String DEFAULT_HOST = "rabbitmq.lappsgrid.org";
//    public static final String DEFAULT_HOST = "localhost"

    public static final String USERNAME_PROPERTY = "RABBIT_USERNAME";
    public static final String PASSWORD_PROPERTY = "RABBIT_PASSWORD";

    public String queueName;
    public String exchange;
    public Connection connection;
    public Channel channel;

    public RabbitMQ(String queueName) throws IOException, TimeoutException
    {
        this(queueName, DEFAULT_HOST);
    }

    public RabbitMQ(String queueName, String host) throws IOException, TimeoutException
    {
        String username = get(USERNAME_PROPERTY);
        String password = get(PASSWORD_PROPERTY);
        init(queueName, host, username, password);
    }

    public RabbitMQ(String queueName, String host, String username, String password) throws IOException, TimeoutException
    {
        init(queueName, host, username, password);
    }

    private void init(String queueName, String host, String username, String password) throws IOException, TimeoutException
    {
        ConnectionFactory factory = new ConnectionFactory();
        String vhost = "/";
        int slash = host.indexOf("/");
        if (slash > 0) {
            vhost = host.substring(slash + 1);
            host = host.substring(0, slash);
        }
        factory.setHost(host);
        factory.setVirtualHost(vhost);
        factory.setUsername(username);
        factory.setPassword(password);
        connection = factory.newConnection();
        channel = connection.createChannel();
        this.queueName = queueName;
    }

    private String get(String name) {
        return RabbitMQ.getProperty(name, "rabbit");
    }

    public static String getHost() {
        return getProperty("RABBIT_HOST", DEFAULT_HOST);
    }

    public static String getUsername() {
        return getProperty("RABBIT_USERNAME", "rabbit");
    }
    public static String getPassword() {
        return getProperty("RABBIT_PASSWORD", "rabbit");
    }

    static String getProperty(String key, String defaultValue) {
        String value = System.getProperty(key);
        if (value != null) {
            System.out.println("Found system property for " + key);
            return value;
        }
        value = System.getenv(key);
        if (value != null) {
            System.out.println("Found environment variable for " + key);
            return value;
        }
        System.out.println("Using default value for " + key);
        return defaultValue;
    }
    /*
    void register(Consumer consumer) {
        channel.basicConsume(queueName, false, consumer)
    }

    void register(Closure cl) {
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                cl(message)
                if (ack) {
                    println "ack ${envelope.deliveryTag}"
                    channel.basicAck(envelope.deliveryTag, false)
                }
            }
        }
        register(consumer)
    }
    */
    public void close() throws IOException, TimeoutException
    {
        if (channel.isOpen()) {
            channel.close();
        }
        if (connection.isOpen()) {
            connection.close();
        }
    }
}
