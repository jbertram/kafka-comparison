package org.keyspace.hornetq;

import org.keyspace.kafka.KafkaConsumer;
import org.keyspace.kafka.KafkaProducer;

public class HornetQTest
{
   public static void main(String[] args) throws Exception
   {
      HornetQConsumer consumerThread = new HornetQConsumer();
      consumerThread.start();

      Thread.sleep(500);

      HornetQProducer producerThread = new HornetQProducer();
      producerThread.start();
   }
}
