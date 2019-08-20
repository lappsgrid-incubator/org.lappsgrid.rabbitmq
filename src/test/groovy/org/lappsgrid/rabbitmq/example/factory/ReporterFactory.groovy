package org.lappsgrid.rabbitmq.example.factory


import org.lappsgrid.rabbitmq.example.Reporter
import org.lappsgrid.rabbitmq.tasks.TaskQueue
import org.lappsgrid.rabbitmq.topic.PostOffice

import java.util.concurrent.CountDownLatch

/**
 *
 */
class ReporterFactory implements IWorkerFactory<Reporter> {

    int id = 0
    CountDownLatch latch

    ReporterFactory(CountDownLatch latch) {
        this.latch = latch
    }

    @Override
    Reporter create(PostOffice po, TaskQueue queue) {
        return new Reporter(++id, latch, po, queue)
    }
}
