package org.lappsgrid.rabbitmq.example.factory


import org.lappsgrid.rabbitmq.example.Sorter
import org.lappsgrid.rabbitmq.tasks.TaskQueue
import org.lappsgrid.rabbitmq.topic.PostOffice

/**
 *  A worker factory that knows how to produce @{link Sorter} instances.
 */
class SorterFactory implements IWorkerFactory<Sorter> {
    int id = 0

    @Override
    Sorter create(PostOffice po, TaskQueue queue) {
        ++id
        return new Sorter(id, queue, po)
    }
}
