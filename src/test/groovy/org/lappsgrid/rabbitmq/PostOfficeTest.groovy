package org.lappsgrid.rabbitmq

import org.junit.BeforeClass
import org.junit.Test
import org.lappsgrid.rabbitmq.topic.MailBox
import org.lappsgrid.rabbitmq.topic.PostOffice

/**
 *
 */
class PostOfficeTest {

    @BeforeClass
    public static void before() {
        File ini = new File("/etc/lapps/askme-dev.ini")
        if (ini.exists()) {
            ini.eachLine { String line ->
                if (!line.startsWith("#") && line.length() > 3) {
                    String[] tokens = line.split('=')
                    if (tokens.length == 2) {
                        System.setProperty(tokens[0], tokens[1])
                    }
                }
            }
        }
    }

    @Test
    void sendMail() {
        int c1 = 0
        int c2 = 0
        MailBox box1 = new MailBox('test.postoffice', 'box1', "rabbitmq.lappsgrid.org/dev") {
            public void recv(String message) {
                println "Box 1 -> $message"
                ++c1
                true
            }
        }

        MailBox box2 = new MailBox('test.postoffice', 'box2', "rabbitmq.lappsgrid.org/dev") {
            public void recv(String message) {
                println "Box 2 -> $message"
                ++c2
                true
            }
        }

        PostOffice post = new PostOffice('test.postoffice', "rabbitmq.lappsgrid.org/dev")
        post.send('box1', "box1 message 1")
        post.send('box2', "box2 message 1")
        post.send('box2', "box2 message 2")
        post.send('box1', "box1 message 2")
        post.send('box2', 'box2 message 3')
        sleep(500)
        post.close()
        box1.close()
        box2.close()
        assert 2 == c1 //box1.count
        assert 3 == c2 //box2.count
        println "Done"

    }
}

/*
class TestBox extends MailBox {
    String id
    int count

    TestBox(String id) {
        super('test.postoffice', id)
        this.id = id
    }

    void recv(String message) {
        ++count
        println "$id -> $message"
    }
}
*/