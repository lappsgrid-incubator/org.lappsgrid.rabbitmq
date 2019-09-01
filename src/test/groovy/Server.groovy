import groovy.json.JsonOutput

import java.util.concurrent.TimeUnit

/**
 * Attempts to use the RabbitMQ API
 */
class Server {

    void print() {
        String json = '[{"cluster_state":{"rabbit@rabbitmq":"running"},"message_stats":{"ack":5947,"ack_details":{"rate":0.0},"confirm":0,"confirm_details":{"rate":0.0},"deliver":6741,"deliver_details":{"rate":0.0},"deliver_get":8890,"deliver_get_details":{"rate":0.0},"deliver_no_ack":2149,"deliver_no_ack_details":{"rate":0.0},"get":0,"get_details":{"rate":0.0},"get_no_ack":0,"get_no_ack_details":{"rate":0.0},"publish":8016,"publish_details":{"rate":0.0},"redeliver":0,"redeliver_details":{"rate":0.0},"return_unroutable":0,"return_unroutable_details":{"rate":0.0}},"messages":0,"messages_details":{"rate":0.0},"messages_ready":0,"messages_ready_details":{"rate":0.0},"messages_unacknowledged":0,"messages_unacknowledged_details":{"rate":0.0},"name":"/","recv_oct":757846025,"recv_oct_details":{"rate":3.2},"send_oct":597582647,"send_oct_details":{"rate":3.2},"tracing":false},{"cluster_state":{"rabbit@rabbitmq":"running"},"message_stats":{"ack":6822,"ack_details":{"rate":0.0},"confirm":0,"confirm_details":{"rate":0.0},"deliver":16918,"deliver_details":{"rate":0.0},"deliver_get":36427,"deliver_get_details":{"rate":0.0},"deliver_no_ack":19509,"deliver_no_ack_details":{"rate":0.0},"get":0,"get_details":{"rate":0.0},"get_no_ack":0,"get_no_ack_details":{"rate":0.0},"publish":42927,"publish_details":{"rate":0.0},"redeliver":0,"redeliver_details":{"rate":0.0},"return_unroutable":0,"return_unroutable_details":{"rate":0.0}},"messages":0,"messages_details":{"rate":0.0},"messages_ready":0,"messages_ready_details":{"rate":0.0},"messages_unacknowledged":0,"messages_unacknowledged_details":{"rate":0.0},"name":"deeplearning","recv_oct":4414807393,"recv_oct_details":{"rate":0.0},"send_oct":4332476314,"send_oct_details":{"rate":0.0},"tracing":false},{"cluster_state":{"rabbit@rabbitmq":"running"},"name":"keith","tracing":false},{"cluster_state":{"rabbit@rabbitmq":"running"},"name":"kevin","tracing":false},{"cluster_state":{"rabbit@rabbitmq":"running"},"message_stats":{"ack":93252,"ack_details":{"rate":0.0},"confirm":0,"confirm_details":{"rate":0.0},"deliver":93434,"deliver_details":{"rate":0.0},"deliver_get":93475,"deliver_get_details":{"rate":0.0},"deliver_no_ack":41,"deliver_no_ack_details":{"rate":0.0},"get":0,"get_details":{"rate":0.0},"get_no_ack":0,"get_no_ack_details":{"rate":0.0},"publish":57109,"publish_details":{"rate":0.0},"redeliver":0,"redeliver_details":{"rate":0.0},"return_unroutable":0,"return_unroutable_details":{"rate":0.0}},"messages":0,"messages_details":{"rate":0.0},"messages_ready":0,"messages_ready_details":{"rate":0.0},"messages_unacknowledged":0,"messages_unacknowledged_details":{"rate":0.0},"name":"nlp","recv_oct":5190927801,"recv_oct_details":{"rate":361.6},"send_oct":7588066492,"send_oct_details":{"rate":177.6},"tracing":false}]'
        println JsonOutput.prettyPrint(json)
    }

    void run() {
        String user = 'rabbit'
        String pass = 'Mi5qrfebcK2I1BEv'
        String host = 'rabbitmq.lappsgrid.org:15672'

        Process process = "curl -i -u ${user}:${pass} -X GET ${host}/api/queues/nlp".execute()
        process.waitFor(30, TimeUnit.SECONDS)
        if (process.exitValue() == 0) {
            String json = process.inputStream.text
            println json
            //println JsonOutput.prettyPrint(json)
        }
        else {
            println "There was an error: ${process.exitValue()}"
            println process.errorStream.text
        }
    }

    static void main(String[] args) {
        new Server().run()
    }
}