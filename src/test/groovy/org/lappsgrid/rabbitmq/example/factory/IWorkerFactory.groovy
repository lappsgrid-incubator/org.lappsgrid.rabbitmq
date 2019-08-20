package org.lappsgrid.rabbitmq.example.factory


import org.lappsgrid.rabbitmq.tasks.TaskQueue
import org.lappsgrid.rabbitmq.topic.PostOffice

/**
 * Factory interface used by {@link QueueManager} instances to spawn new workers.
 */
interface IWorkerFactory<T> {
    T create(PostOffice po, TaskQueue queue)
}