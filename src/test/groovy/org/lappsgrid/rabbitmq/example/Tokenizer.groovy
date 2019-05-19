package org.lappsgrid.rabbitmq.example


import org.lappsgrid.serialization.Serializer
import org.lappsgrid.rabbitmq.Message
import org.lappsgrid.rabbitmq.tasks.TaskQueue
import org.lappsgrid.rabbitmq.tasks.Worker

/**
 * Tokenizes the message body on whitespace using the String.split() method.
 */
class Tokenizer extends Worker {

    int id
    org.lappsgrid.rabbitmq.topic.PostOffice po

    Tokenizer(int id, TaskQueue queue, org.lappsgrid.rabbitmq.topic.PostOffice po) {
        super(queue)
        this.id = id
        this.po = po
    }

    @Override
    void work(String json) {
        // Parse the message.
        Message message = Serializer.parse(json, Message)
        // Do the work.
        String msgid = message.get("id")
        println "Tokenizer $id : message $msgid"
        String[] tokens = message.body.split()
        message.body(tokens)
        // Forward the message.
        po.send(message)
        stats()
    }

    void stats() {
        Map data = [
                id: "Tokenizer $id",
                thread: Thread.currentThread().name
        ]
        Message message = new Message().body(data).route('stats.mbox')
        po.send(message)
    }
}
