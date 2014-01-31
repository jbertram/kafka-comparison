package org.keyspace.kafka;

public class KafkaTest
{
   public static void main(String[] args) throws Exception
   {
      KafkaConsumer consumerThread = new KafkaConsumer();
      consumerThread.start();

      Thread.sleep(500);

      KafkaProducer producerThread = new KafkaProducer();
      producerThread.start();
   }
}
