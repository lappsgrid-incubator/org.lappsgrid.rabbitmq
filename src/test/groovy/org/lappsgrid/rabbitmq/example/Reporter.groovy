package org.lappsgrid.rabbitmq.example


import org.lappsgrid.serialization.Serializer
import org.lappsgrid.rabbitmq.Message
import org.lappsgrid.rabbitmq.tasks.Worker
import org.lappsgrid.rabbitmq.tasks.TaskQueue

import java.util.concurrent.CountDownLatch

/**
 * The Reporter is the final worker in the pipeline and simply writes the message to System.out as JSON.
 */
class Reporter extends Worker {

    int id
    CountDownLatch latch
    org.lappsgrid.rabbitmq.topic.PostOffice po

    Reporter(int id, CountDownLatch latch, org.lappsgrid.rabbitmq.topic.PostOffice po, TaskQueue queue) {
        super(queue)
        this.id = id
        this.po = po
        this.latch = latch
    }

    @Override
    void work(String json) {
        Message message = Serializer.parse(json, Message)
        message.set("reporter", "$id")
        println Serializer.toPrettyJson(message)
        latch.countDown()
        stats()
    }

    void stats() {
        Map data = [
                id: "Reporter $id",
                thread: Thread.currentThread().name
        ]
        Message message = new Message().body(data).route('stats.mbox')
        po.send(message)
    }

}
