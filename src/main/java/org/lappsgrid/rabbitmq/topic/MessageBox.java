package org.lappsgrid.rabbitmq.topic;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import groovy.lang.MetaClass;
import org.lappsgrid.rabbitmq.Message;
import org.lappsgrid.rabbitmq.RabbitMQ;
import org.lappsgrid.serialization.Serializer;

import java.io.IOException;

/**
 *
 * See: https://issues.apache.org/jira/browse/GROOVY-7362
 * See: https://issues.apache.org/jira/browse/GROOVY-8495
 */
public abstract class MessageBox extends RabbitMQ
{
	private String exchange;

	public MessageBox(String exchange, String address) throws IOException
	{
		this(exchange, address, RabbitMQ.DEFAULT_HOST);
	}

	public MessageBox(String exchange, String address, String host) throws IOException
	{
		super("", host);
		Channel channel = super.getChannel();

		channel.exchangeDeclare(exchange, "direct");
		boolean passive = false;
		boolean durable = true;
		boolean exclusive = false;
		boolean autoDelete = true;
		this.setQueueName(channel.queueDeclare("", durable, exclusive, autoDelete, null).getQueue());
		channel.queueBind(this.getQueueName(), exchange, address);
		channel.basicConsume(this.getQueueName(), false, new MessageBoxConsumer(this));
	}

	public abstract void recv(Message message);

	class MessageBoxConsumer extends DefaultConsumer
	{
		private MessageBox box;

		MessageBoxConsumer(MessageBox box) {
			super(box.getChannel());
			this.box = box;
		}

		public void handleDelivery(String consumerTag, Envelope envelope,
							AMQP.BasicProperties properties, byte[] body)
				throws IOException
		{
			String json = new String(body, "UTF-8");
			try {
				Message message = Serializer.parse(json, Message.class);
				box.recv(message);
				this.getChannel().basicAck(envelope.getDeliveryTag(), false);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	@Override
	public Object invokeMethod(String s, Object o)
	{
		return null;
	}

	@Override
	public Object getProperty(String s)
	{
		return null;
	}

	@Override
	public void setProperty(String s, Object o)
	{

	}

	@Override
	public MetaClass getMetaClass()
	{
		return null;
	}

	@Override
	public void setMetaClass(MetaClass metaClass)
	{

	}


}
