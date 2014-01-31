package org.keyspace.kafka;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import org.apache.log4j.Logger;
import org.keyspace.TestProperties;

public class KafkaConsumer extends Thread
{
   private static final Logger logger = Logger.getLogger(KafkaConsumer.class);

   public void run()
   {
      logger.info("Kafka consumer thread started.");
      Properties props = new Properties();
      props.put("zookeeper.connect", System.getProperty(TestProperties.ZOOKEEPER, TestProperties.DEFAULT_ZOOKEEPER));
      props.put("group.id", "group1");
      props.put("zookeeper.session.timeout.ms", "400");
      props.put("zookeeper.sync.time.ms", "200");
      props.put("auto.commit.interval.ms", "1000");

      ConsumerConfig consumerConfig = new ConsumerConfig(props);

      ConsumerConnector consumer = Consumer.createJavaConsumerConnector(consumerConfig);

      Map<String, Integer> topicCountMap = new HashMap<>();
      String topic = System.getProperty(TestProperties.TOPIC, TestProperties.DEFAULT_TOPIC);
      topicCountMap.put(topic, 1);
      Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap = consumer.createMessageStreams(topicCountMap);
      KafkaStream<byte[], byte[]> stream = consumerMap.get(topic).get(0);
      ConsumerIterator<byte[], byte[]> it = stream.iterator();
      long start = 0;
      long elapsedTime;
      long size = Long.parseLong(System.getProperty(TestProperties.SIZE, Long.toString(TestProperties.DEFAULT_SIZE)));
      for (int i = 0; i < size; i++)
      {
         it.next().message();
         if (i == 0)
         {
            /*
             * The consumer won't actually start its work until the producer starts sending its messages
             * therefore we don't start the timer until messages actually begin to flow.
             */
            logger.info("Kafka consumer started receiving messages.  Starting timer...");
            start = System.currentTimeMillis();
         }
      }

      elapsedTime = System.currentTimeMillis() - start;
      logger.info("Consumed " + size + " messages in " + elapsedTime + "ms; " + (size / ((float)elapsedTime / 1000)) + " msgs/sec");
      consumer.shutdown();
   }
}
