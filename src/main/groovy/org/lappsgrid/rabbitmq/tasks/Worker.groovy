package org.lappsgrid.rabbitmq.tasks

import com.rabbitmq.client.AMQP
import com.rabbitmq.client.DefaultConsumer
import com.rabbitmq.client.Envelope
import org.lappsgrid.rabbitmq.RabbitMQ

/**
 *
 */
abstract class Worker extends DefaultConsumer {

    protected TaskQueue queue
    // Only close the queue if we created the queue.
    protected boolean autoclose = false

    Worker(String name) {
        this(new TaskQueue(name, RabbitMQ.Context.host))
        autoclose = true
    }
    /**
     * TODO: This constructor is likely wonky... if the provided parameter is not the same
     * as RabbitMQ.Context.host then the behaviour is undefined.
     *
     */
    @Deprecated
    Worker(String name, String host) {
        this(new TaskQueue(name, host))
        autoclose = true
    }
    
    Worker(TaskQueue queue) {
        super(queue.channel)
        queue.register(this)
        this.queue = queue
    }

    void close() {
        if (autoclose) {
            queue.close()
        }
    }

    void handleDelivery(String consumerTag, Envelope envelope,
                        AMQP.BasicProperties properties, byte[] body)
            throws IOException {
        String message = new String(body, "UTF-8");
        try {
            work(message)
        }
        catch (Exception e) {
            // TODO Add proper logging.
            System.err.println("Error working on message: ${e.message}")
            e.printStackTrace()
        }
//            this.channel.basicAck(envelope.deliveryTag, false)
    }

    abstract void work(String message)

}
