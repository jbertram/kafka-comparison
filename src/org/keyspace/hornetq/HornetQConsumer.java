package org.keyspace.hornetq;

import javax.jms.Connection;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;
import java.util.HashMap;

import org.hornetq.api.core.TransportConfiguration;
import org.hornetq.api.jms.HornetQJMSClient;
import org.hornetq.api.jms.JMSFactoryType;
import org.hornetq.core.remoting.impl.netty.NettyConnectorFactory;
import org.hornetq.jms.client.HornetQConnectionFactory;
import org.jboss.logging.Logger;
import org.keyspace.TestProperties;

public class HornetQConsumer extends Thread
{
   private static final Logger logger = Logger.getLogger(HornetQConsumer.class);

   public void run()
   {
      logger.info("HornetQ consumer thread started.");
      HashMap<String, Object> params = new HashMap<>();
      params.put("host", System.getProperty(TestProperties.HORNETQ_ADDRESS, TestProperties.DEFAULT_HORNETQ_ADDRESS));
      params.put("port", System.getProperty(TestProperties.HORNETQ_PORT, TestProperties.DEFAULT_HORNETQ_PORT));
      TransportConfiguration transportConfiguration = new TransportConfiguration(NettyConnectorFactory.class.getName(), params);

      HornetQConnectionFactory connectionFactory = HornetQJMSClient.createConnectionFactoryWithoutHA(JMSFactoryType.CF, transportConfiguration);

      try (Connection connection = connectionFactory.createConnection())
      {
         connection.start();
         Session session = connection.createSession(false, Session.CLIENT_ACKNOWLEDGE);
         MessageConsumer producer = session.createConsumer(HornetQJMSClient.createTopic(System.getProperty(TestProperties.TOPIC, TestProperties.DEFAULT_TOPIC)));
         long start = 0;
         long elapsedTime;
         Message m;
         long size = Long.parseLong(System.getProperty(TestProperties.SIZE, Long.toString(TestProperties.DEFAULT_SIZE)));
         for (int i = 0; i < size; i++)
         {
            m = producer.receive();
            if (i == 0)
            {
               logger.info("HornetQ consumer started receiving messages.  Starting timer...");
               start = System.currentTimeMillis();
            }
            if (i % 1000 == 0 && m != null)
            {
               m.acknowledge();
            }
         }

         elapsedTime = System.currentTimeMillis() - start;
         logger.info("Consumed " + size + " messages in " + elapsedTime + "ms; " + (size / ((float)elapsedTime / 1000)) + " msgs/sec");
         connection.close();
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }
}
