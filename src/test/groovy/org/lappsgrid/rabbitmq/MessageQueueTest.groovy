package org.lappsgrid.rabbitmq

import org.junit.*
import org.lappsgrid.rabbitmq.tasks.TaskQueue
import org.lappsgrid.rabbitmq.tasks.Worker

import java.util.concurrent.atomic.AtomicInteger

/**
 *
 */
class MessageQueueTest {

    TaskQueue queue

    @BeforeClass
    static void init() {
        System.setProperty(RabbitMQ.USERNAME_PROPERTY, "guest")
        System.setProperty(RabbitMQ.PASSWORD_PROPERTY, "guest")
//        Process proc = "docker -d -p 5672:5672 --hostname test-rabbit --name test-rabbit rabbitmq:3".execute()
//        sleep(1000)
    }

//    @AfterClass
//    static void shutdown() {
//        "docker rm -f test-rabbit".execute()
//    }

    @Before
    void setup() {
        queue = new TaskQueue('test.queue', 'localhost')
    }

    @After
    void teardown() {
        queue.close()
        queue = null
    }

    @Test
    void simple() {
        int n = 0
//        TaskQueue q = new TaskQueue('test.queue')
        queue.register { String message ->
            println "Received: $message"
            ++n
        }

        queue.send('hello world')
        sleep(500)
        assert 1 == n
    }

    @Test
    void send() {
        AtomicInteger count = new AtomicInteger()
        queue.register { String message ->
            count.incrementAndGet()
            println "Registered closure: $message"
        }
        sleep(500)
        queue.send("1. Hello world.")
        queue.send("2. Hello world.")
        queue.send("3. Hello world.")
        println "Messages sent"
        sleep(1000)
        assert 3 == count.intValue()
    }

    @Test
    void workers() {
//        TaskQueue q = new TaskQueue('test.queue')
        AtomicInteger n = new AtomicInteger()
        queue.register { msg ->
            int local_n = n.incrementAndGet()
            println "worker 1[$local_n] $msg"
        }
        TestWorker w = new TestWorker(queue)
        sleep(500)
        queue.send("one")
        queue.send("two")
        queue.send("three")
        queue.send("four")
        Thread.sleep(1000)
        assert 2 == w.n
        assert 2 == n.intValue()

        println "Done"
    }

    class TestWorker extends Worker {
        int n

        TestWorker(TaskQueue q) {
            super(q)
        }

        @Override
        void work(String message) {
            ++n
            println "worker 2[$n] $message"
        }
    }
}
