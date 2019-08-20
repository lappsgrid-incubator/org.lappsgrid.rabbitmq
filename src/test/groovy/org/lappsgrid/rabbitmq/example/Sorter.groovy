package org.lappsgrid.rabbitmq.example


import org.lappsgrid.serialization.Serializer
import org.lappsgrid.rabbitmq.Message
import org.lappsgrid.rabbitmq.tasks.TaskQueue
import org.lappsgrid.rabbitmq.tasks.Worker

/**
 * Sorts the message body, which is expected to be an array of strings.
 */
class Sorter extends Worker {
    int id
    org.lappsgrid.rabbitmq.topic.PostOffice po

    Sorter(int id, TaskQueue queue, org.lappsgrid.rabbitmq.topic.PostOffice po) {
        super(queue)
        this.id = id
        this.po = po
    }

    @Override
    void work(String json) {
        // Parse the message
        Message message = Serializer.parse(json, Message)
        // Do the work
        String msgid = message.get("id")
        println "Sorter $id message: $msgid"
        String[] tokens = (String[]) message.body
        message.body = tokens.sort()
        // And forward the message
        po.send(message)
        stats()
    }
    void stats() {
        Map data = [
                id: "Sorter $id",
                thread: Thread.currentThread().name
        ]
        Message message = new Message().body(data).route('stats.mbox')
        po.send(message)
    }
}
