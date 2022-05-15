package com.example.demo.client;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class KafkaConsumerClient {

    Properties props;
    KafkaConsumer<String, String> consumer;

    public KafkaConsumerClient() {
        props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("group.id", "test-group");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        consumer = new KafkaConsumer<>(props);
    }

    public ResponseEntity readMessage(String topic, int limit) {
        consumer.subscribe(Collections.singleton(topic));

        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

        int skip = getSkipNumber(limit, records.count()); // calc how many records to skip

        List<String> messages =
                StreamSupport.stream(records.records(topic).spliterator(), false)
                        .skip(skip)
                        .map(ConsumerRecord::value) // extract the messages
                        .collect(Collectors.toList());

        if (messages.size() == 0)
            return new ResponseEntity<>("No New Messages Found for topic: " + topic, HttpStatus.OK);
        else
            return new ResponseEntity<>("Messages for topic: " + topic + messages, HttpStatus.OK);
    }

    private int getSkipNumber(int limit, int count) {
        if (limit > count)
            return 0; // if given limit is more than existing records, do not skip anything
        else
            return count - limit;
    }
}
