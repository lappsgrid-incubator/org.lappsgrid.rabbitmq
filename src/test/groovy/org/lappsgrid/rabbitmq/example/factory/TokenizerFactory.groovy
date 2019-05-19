package org.lappsgrid.rabbitmq.example.factory


import org.lappsgrid.rabbitmq.example.Tokenizer
import org.lappsgrid.rabbitmq.tasks.TaskQueue
import org.lappsgrid.rabbitmq.topic.PostOffice

/**
 *  A worker factory for creating Tokenizers.
 */
class TokenizerFactory implements IWorkerFactory<Tokenizer> {
    int id = 0

    @Override
    Tokenizer create(PostOffice po, TaskQueue queue) {
        ++id
        return new Tokenizer(id, queue, po)
    }
}
