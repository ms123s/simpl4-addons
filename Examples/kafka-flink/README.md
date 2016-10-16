# kafka-flink-example
Simple example for reading and writing into Kafka

This work is based on http://data-artisans.com/kafka-flink-a-practical-how-to/


```bash
# Make sure karaf is running

#we need a kafka client
wget http://apache.lauf-forum.at/kafka/0.10.0.1/kafka_2.10-0.10.0.1.tgz
# unpack
tar xf kafka_2.10-0.10.0.1
cd kafka_2.10-0.10.0.1


# Compile the examples
./build.sh

# create topic “test”
./bin/kafka-topics.sh --create --topic test --zookeeper localhost:2181/kafka --partitions 1 --replication-factor 1

# ReadFromKafka
./run-read.sh

# In a other terminal: produce something into the topic (write something and hit enter)
./bin/kafka-console-producer.sh --topic test --broker-list localhost:9092

# WriteIntoKafka
./run-write.sh

# In a other terminal: consume from the topic using the console producer
./bin/kafka-console-consumer.sh --topic test --zookeeper localhost:2181/kafka

```
