## Kafka Comparison

This is an *extremely simple* producer/consumer test for Kafka and HornetQ to compare relative performance. A general
discussion of what Kafka and HornetQ are is beyond the scope of this document. See these websites for more general information:

* http://www.jboss.org/hornetq/
* https://kafka.apache.org/

The current configuration of this project compares Kafka 0.8 with HornetQ 2.4.0.Final.  Kafka is not yet in Maven so the
project uses a simple Ant build (see build.xml).

### What this test exercises

The following can be simply executed for either Kafka or HornetQ:

* Launch a single consumer thread
* Launch single producer thread
* Producer thread will send an arbitrary number of *empty* messages as fast as it can
* Consumer thread will fetch these messages messages as fast as it can

The process is timed and a simple summary is printed at the end of the test.

It's as simple as that.

To be clear, the semantics of Kafka and JMS consumers and producers are not 1:1, but this test tries to make them as
close as possible.

For example, a Kafka consumer can not acknowledge an individual message (which is the customary behavior
for a JMS client). Rather a Kafka consumer uses a timer (controlled by "auto.commit.enable" and "auto.commit.interval.ms"
properties on the consumer). When the configured time elapses all the messages consumed in a given time-frame are
acknowledged. See [the Kafka documentation on consumers](http://kafka.apache.org/documentation.html#theconsumer) for
more details on how this design choice. To approximate this behavior the HornetQ consumer uses the "client acknowledgement"
mode and acknowledges batches of 1000 messages.

Furthermore, Kafka consumers have no concept of callbacks (i.e. the server can't "push" messages to the client as done
with a JMS [MessageListener](http://docs.oracle.com/javaee/7/api/javax/jms/MessageListener.html)). Therefore, the HornetQ
consumer uses a [MessageConsumer](http://docs.oracle.com/javaee/7/api/javax/jms/MessageConsumer.html).

### How to run the Kafka test

* [Download Kafka 0.8](https://kafka.apache.org/downloads.html)
* [Install and start Kafka](https://kafka.apache.org/documentation.html#quickstart)
* Create a topic to be used for the test. By default, "testTopic" will be used. For example:

     ```bin/kafka-create-topic.sh --zookeeper localhost:2181 --replica 1 --partition 1 --topic testTopic```

* Compile this project:

     ```ant compile```

* Run the Kafka test:

     ```ant runKafkaTest```


### How to run the HornetQ test

* [Download HornetQ 2.4.0.Final](http://www.jboss.org/hornetq/downloads)
* Unzip the HornetQ distribution to &lt;HORNETQ_HOME&gt;
* Add this to &lt;HORNETQ_HOME&gt;/config/stand-alone/non-clustered/hornetq-jms.xml:
```
     <topic name="testTopic">
        <entry name="/topic/testTopic"/>
     </topic>
```
* Start HornetQ:

     ```bin/run.sh```

* Compile this project:

     ```ant compile```

* Run the HornetQ test:

     ```ant runHornetQTest```

### Configuring the tests

Inspect build.xml for configuration options.