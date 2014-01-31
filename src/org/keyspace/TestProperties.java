package org.keyspace;

public class TestProperties
{
   public static final String ZOOKEEPER = "consumerZookeeper";
   public static final String DEFAULT_ZOOKEEPER = "127.0.0.1:2181";

   public static final String BROKER_LIST = "producerBrokerList";
   public static final String DEFAULT_BROKER_LIST = "localhost:9092";

   public static final String SIZE = "testSize";
   public static final long DEFAULT_SIZE = 10000;

   public static final String TOPIC = "topic";
   public static final String DEFAULT_TOPIC = "testTopic";

   public static final String PRODUCER_ACK_MODE = "producerAckMode";
   public static final String DEFAULT_PRODUCER_ACK_MODE = "1";

   public static final String HORNETQ_ADDRESS = "hornetQAddress";
   public static final String DEFAULT_HORNETQ_ADDRESS = "127.0.0.1";

   public static final String HORNETQ_PORT = "hornetQPort";
   public static final String DEFAULT_HORNETQ_PORT = "5445";
}
