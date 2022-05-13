package com.example.demo.client;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.UUID.randomUUID;

public class KafkaConsumerClient {

    Properties props;
    KafkaConsumer<String, String> consumer;

    public KafkaConsumerClient() {
        props = new Properties();
        props.setProperty("bootstrap.servers", "localhost:9092");
        props.setProperty("group.id", randomUUID().toString());
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        consumer = new KafkaConsumer<>(props);
    }

    public ResponseEntity readMessage(String topic, int limit) {
        consumer.subscribe(Collections.singleton(topic));

        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));

        int skip = getSkipNumber(limit, records.count());

        List<ConsumerRecord<String, String>> result =
                StreamSupport.stream(records.records(topic).spliterator(), false)
                        .skip(skip)
                        .collect(Collectors.toList());



        if (result.size() == 0)
            return new ResponseEntity<>("No Messages Found for " + topic  ,HttpStatus.INTERNAL_SERVER_ERROR);
        else
            return new ResponseEntity<>("Messages for " + topic + result, HttpStatus.OK);

    }

    private int getSkipNumber (int limit, int count)
    {
        if (limit > count)
        {
            return 0;
        }
        else
        {
            return count - limit;
        }
    }

}
