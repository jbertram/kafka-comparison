package org.keyspace.hornetq;

import javax.jms.Connection;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import java.util.HashMap;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.jms.HornetQJMSClient;
import org.hornetq.api.jms.JMSFactoryType;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.hornetq.jms.client.HornetQConnectionFactory;
import org.jboss.logging.Logger;
import org.keyspace.TestProperties;

public class HornetQProducer extends Thread
{
   private static final Logger logger = Logger.getLogger(HornetQProducer.class);

   public void run()
   {
      logger.info("HornetQ producer thread started.");
      HashMap<String, Object> params = new HashMap<>();
      params.put("host", System.getProperty(TestProperties.HORNETQ_ADDRESS, TestProperties.DEFAULT_HORNETQ_ADDRESS));
      params.put("port", System.getProperty(TestProperties.HORNETQ_PORT, TestProperties.DEFAULT_HORNETQ_PORT));
      TransportConfiguration transportConfiguration = new TransportConfiguration(NettyConnectorFactory.class.getName(), params);

      HornetQConnectionFactory connectionFactory = HornetQJMSClient.createConnectionFactoryWithoutHA(JMSFactoryType.CF, transportConfiguration);

      try (Connection connection = connectionFactory.createConnection())
      {
         Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
         MessageProducer producer = session.createProducer(HornetQJMSClient.createTopic(System.getProperty(TestProperties.TOPIC, TestProperties.DEFAULT_TOPIC)));
         long size = Long.parseLong(System.getProperty(TestProperties.SIZE, Long.toString(TestProperties.DEFAULT_SIZE)));
         long elapsedTime;
         Message m = session.createTextMessage();
         long start = System.currentTimeMillis();
         for (int i = 0; i < size; i++)
         {
            producer.send(m);
         }

         elapsedTime = System.currentTimeMillis() - start;
         logger.info("Produced " + size + " messages in " + elapsedTime + "ms; " + (size / ((float)elapsedTime / 1000)) + " msgs/sec");
         connection.close();
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }
}
