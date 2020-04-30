package org.lappsgrid.rabbitmq.askme

import groovy.json.JsonOutput
import org.junit.Ignore
import org.junit.Test
import org.lappsgrid.serialization.Serializer

/**
 *
 */
@Ignore
class AskmeTest {

    @Test
    void serializeMessage() {
        AskmeMessage message = createMessage()
        String json = JsonOutput.toJson(message)
        AskmeMessage msg = Serializer.parse(json, AskmeMessage)

    }

    AskmeMessage createMessage() {
        AskmeMessage message = new AskmeMessage().command('proc').route('home')
        Packet packet = new Packet()
        packet.query = new Query()
        packet.query.with {
            query = 'the query'
            question = 'the question'
            terms = [ 'the', 'term', 'list' ]
        }
        packet.documents = []
        packet.documents.add(createDocument('doc-1'))
        packet.documents.add(createDocument('doc-2'))
        packet.documents.add(createDocument('doc-3'))
        message.body = packet
        return message
    }

    Document createDocument(String id) {
        Document document = new Document()
        document.id = id
        document.title = createSection('hello ' + id)
        document.article = createSection("This is the body of article $id")
        return document
    }

    Section createSection(String text) {
        Section section = new Section()
        section.text = text
        section.tokens = []
        Sentence sentence = new Sentence()
        sentence.tokens = []
        section.text = text
        text.tokenize(' ').each {
            Token token = new Token()
            token.word = it
            token.start = token.end = 1
            section.tokens.add(token)
            sentence.tokens.add(token)
        }
        section.sentences = [ sentence ]
        return section
    }
}
