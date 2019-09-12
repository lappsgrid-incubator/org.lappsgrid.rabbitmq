package org.lappsgrid.rabbitmq

import org.junit.Test
import org.lappsgrid.serialization.Serializer

/**
 *
 */
class MessageTest {

    @Test
    void route() {
        Message message = new Message().route('a', 'b', 'c')
        assert ['a','b','c'] == message.route
    }

    @Test
    void messageGenerics() {
        TestData data = new TestData()
        data.list.add('one')
        data.list.add('two')
        data.list.add('three')
        data.map['key'] = 'value'
        data.map.value = 'key'
        data.string = 'hello world'
        data.number = 42

        Message<TestData> message = new Message<TestData>().command('test').body(data).route('box1', 'box2')

        String json = Serializer.toJson(message)

        Message<TestData> tm = Serializer.parse(json, TestMessage)
        println "Class: ${tm.body}"
        TestData d = tm.body
        assert ['one', 'two', 'three'] == d.list
        assert 'value' == d.map.key
        assert 'key' == d.map.value
        assert 2 == d.map.size()
        assert 'hello world' == d.string
        assert 42 == d.number

    }

    @Test
    void sendGenericMessage() {

    }
}

class TestData {
    List<String> list = []
    Map<String,String> map = [:]
    String string
    Integer number
}

class TestMessage extends Message<TestData> {}