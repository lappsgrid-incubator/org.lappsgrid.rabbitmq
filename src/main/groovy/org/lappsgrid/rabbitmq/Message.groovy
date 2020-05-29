package org.lappsgrid.rabbitmq

/**
 * A convenience class for passing messages between processes using RabbitMQ.
 *
 * The <code>command</code>, <code>body</code>, and <code>parameters</code> are
 * application dependent and no restrictions are placed on their use or meaning.
 * The <code>route</code> field is used by the framework to determine the next
 * mailbox (RabbitMQ message queue) the message will be sent to.
 */
class Message<T> {
    String id
    String command
    T body
    List<String> route
    Map<String,String> parameters

    Message() {
        id = UUID.randomUUID().toString()
        command = ''
        route = []
        body = null
        parameters = [:]
    }

    Message(String command, T body, String... route) {
        this(command, body, [:], route.toList())
    }

    Message(String command, T body, Map<String,String> parameters, String... route) {
        this(command, body, route.toList())
        this.parameters = parameters
    }

    Message(String command, T body, List<String> route) {
        this(command, body, [:], route)
    }

    Message(String command, T body, Map<String, String> parameters, List<String> route) {
        this.id = UUID.randomUUID().toString()
        this.command = command
        this.body = body
        this.route = route
        this.parameters = parameters
    }

    Message command(String command) { this.command = command ; this }
    Message body(T body)       { this.body = body       ; this }
    Message route(String route)     { this.route.add(route)  ; this}
    Message route(String... route) {
        route.each { this.route.add(it) }
        return this
    }
    Message set(String name, String value) { this.parameters[name] = value ;  this }
    String get(String key) {
        return parameters[key]
    }

    /**
     * Does this message have somewhere to go?
     *
     * @return true if route[] is not null and contains at least one entry.
     */
    boolean routable() {
        return route != null && route.size() > 0
    }
}
