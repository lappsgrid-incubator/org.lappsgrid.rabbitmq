package org.lappsgrid.rabbitmq

import org.junit.*
import org.lappsgrid.rabbitmq.tasks.TaskQueue
import org.lappsgrid.rabbitmq.tasks.Worker

import java.util.concurrent.atomic.AtomicInteger

/**
 *
 */
@Ignore
class MessageQueueTest {

    TaskQueue queue

    @BeforeClass
    public static void before() {
//        File ini = new File("/etc/lapps/askme-dev.ini")
//        if (ini.exists()) {
//            ini.eachLine { String line ->
//                if (!line.startsWith("#") && line.length() > 3) {
//                    String[] tokens = line.split('=')
//                    if (tokens.length == 2) {
//                        System.setProperty(tokens[0], tokens[1])
//                    }
//                }
//            }
//        }
        System.setProperty("RABBIT_HOST", "localhost")
        System.setProperty("RABBIT_USERNAME", "guest")
        System.setProperty("RABBIT_PASSWORD", "guest")
        System.setProperty("RABBIT_EXCHANGE", "askme_dev")

    }

    @Before
    void setup() {
        queue = new TaskQueue('test.queue')
    }

    @After
    void teardown() {
        queue.close()
        queue = null
    }

    @Test
    void simple() {
        int n = 0
        queue.register { String message ->
            println "Received: $message"
            ++n
        }
        queue.send("1. hello world")
        queue.send("2. hello world")
        sleep(1000)
        assert 2 == n
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
