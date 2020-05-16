package org.lappsgrid.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * A base class that handles RabbitMQ connections, channels, exchanges, and queues.
 */
public class RabbitMQ {
    static class Context {
        public static String host = "localhost";
        public static String username = "guest";
        public static String password = "guest";
    }

    public static final String HOST_PROPERTY = "RABBIT_HOST";
    public static final String USERNAME_PROPERTY = "RABBIT_USERNAME";
    public static final String PASSWORD_PROPERTY = "RABBIT_PASSWORD";

    public String queueName;
//    public final String exchange;
    public Connection connection;
    public Channel channel;

    public RabbitMQ(String queueName) throws IOException, TimeoutException
    {
        this.queueName = queueName;
        configure();
        connect();
    }

    public RabbitMQ(String queueName, String host) throws IOException, TimeoutException
    {
        this.queueName = queueName;
        configure();
        Context.host = host;
        connect();
    }

    public RabbitMQ(String queueName, String address, String username, String password) throws IOException, TimeoutException {
        this.queueName = queueName;
        Context.host = address;
        Context.username = username;
        Context.password = password;
        connect();
    }

    private void connect() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        String host = Context.host;
        String vhost = "/";
        int slash = host.indexOf("/");
        if (slash > 0) {
            vhost = host.substring(slash + 1);
            host = host.substring(0, slash);
        }
        factory.setHost(host);
        factory.setVirtualHost(vhost);
        factory.setUsername(Context.username);
        factory.setPassword(Context.password);
        connection = factory.newConnection();
        channel = connection.createChannel();
    }

    public static void configure(String host, String username, String password) {
        Context.host = host;
        Context.username = username;
        Context.password = password;
    }

    private void configure() {
        Context.host = getProperty(HOST_PROPERTY, Context.host);
        Context.username = getProperty(USERNAME_PROPERTY, Context.username);
        Context.password = getProperty(PASSWORD_PROPERTY, Context.password);
    }

    private String get(String name) {
        return RabbitMQ.getProperty(name, "rabbit");
    }

    public static String getHost() {
        return getProperty("RABBIT_HOST", Context.host);
    }
    public static String getUsername() {
        return getProperty("RABBIT_USERNAME", Context.username);
    }
    public static String getPassword() {
        return getProperty("RABBIT_PASSWORD", Context.password);
    }

    static String getProperty(String key, String defaultValue) {
        String value = System.getProperty(key);
        if (value != null) {
            return value;
        }
        value = System.getenv(key);
        if (value != null) {
            return value;
        }
        return defaultValue;
    }

    public void close() throws IOException, TimeoutException
    {
        if (channel.isOpen()) {
            channel.close();
        }
        channel = null;
        if (connection.isOpen()) {
            connection.close();
        }
        connection = null;
    }
}
