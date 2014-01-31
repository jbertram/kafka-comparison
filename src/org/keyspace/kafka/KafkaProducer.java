package org.keyspace.kafka;

import java.util.Properties;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import org.apache.log4j.Logger;
import org.keyspace.TestProperties;

public class KafkaProducer extends Thread
{
   private static final Logger logger = Logger.getLogger(KafkaProducer.class);

   public void run()
   {
      logger.info("Kafka producer thread started.");
      Properties props = new Properties();
      props.put("serializer.class", "kafka.serializer.StringEncoder");
      props.put("metadata.broker.list", System.getProperty(TestProperties.BROKER_LIST, TestProperties.DEFAULT_BROKER_LIST));
      props.put("request.required.acks", System.getProperty(TestProperties.PRODUCER_ACK_MODE, TestProperties.DEFAULT_PRODUCER_ACK_MODE));
      ProducerConfig config = new ProducerConfig(props);
      Producer<String, String> producer = new Producer<>(config);
      String topic = System.getProperty(TestProperties.TOPIC, TestProperties.DEFAULT_TOPIC);
      KeyedMessage<String, String> keyedMessage = new KeyedMessage<>(topic, "");
      long elapsedTime;
      long size = Long.parseLong(System.getProperty(TestProperties.SIZE, Long.toString(TestProperties.DEFAULT_SIZE)));
      long start = System.currentTimeMillis();
      for (int i = 0; i < size; i++)
      {
         producer.send(keyedMessage);
      }
      elapsedTime = System.currentTimeMillis() - start;
      producer.close();

      logger.info("Produced " + size + " messages in " + elapsedTime + "ms; " + (size / ((float)elapsedTime / 1000)) + " msgs/sec");
   }
}
