package org.lappsgrid.rabbitmq.askme

import org.lappsgrid.rabbitmq.Message

/**
 *
 */
class AskmeMessage extends Message<Packet> {

    AskmeMessage() {
    }

    AskmeMessage(String command, Packet body, String... route) {
        super(command, body, route)
    }

    AskmeMessage(String command, Packet body, Map<String, String> parameters, String... route) {
        super(command, body, parameters, route)
    }

    AskmeMessage(String command, Packet body, List<String> route) {
        super(command, body, route)
    }

    AskmeMessage(String command, Packet body, Map<String, String> parameters, List<String> route) {
        super(command, body, parameters, route)
    }
}
