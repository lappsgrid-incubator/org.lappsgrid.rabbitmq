package org.lappsgrid.rabbitmq.topic


import com.rabbitmq.client.MessageProperties
import org.lappsgrid.rabbitmq.Message
import org.lappsgrid.rabbitmq.RabbitMQ
import org.lappsgrid.serialization.Serializer

/**
 *
 */
class PostOffice extends RabbitMQ {
    String exchange

    PostOffice(String exchange) {
        this(exchange, RabbitMQ.DEFAULT_HOST)
    }

    PostOffice(String exchange, String host) {
        super('', host)
        this.exchange = exchange
        channel.exchangeDeclare(exchange, 'direct')
    }

    void send(Message message) {
        if (message.route.size() == 0) {
            println "Nowhere to send message"
            return
        }
        String address = message.route.remove(0)
        String json = Serializer.toJson(message) //JsonOutput.toJson(message)
        send(address, json)
    }

    void send(String address, String message) {
//        println "Sending ${message.bytes.length} bytes to $address"
//        channel.basicPublis,h(exchange, address, MessageProperties.PERSISTENT_TEXT_PLAIN, message.bytes)
        send(address, message.bytes)
    }

    void send(String address, byte[] data) {
        channel.basicPublish(exchange, address, MessageProperties.PERSISTENT_TEXT_PLAIN, data)
    }
}
