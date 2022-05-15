package com.example.demo.client;

import org.apache.kafka.clients.producer.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Properties;

public class KafkaProducerClient {

    Producer<String, String> producer;
    Properties props;

    public KafkaProducerClient() {

        props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        props.put("acks", "all");
        props.put("retries", 0);
        props.put("linger.ms", 1);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    }

    public ResponseEntity insertMessage(String topic, String input) {

        producer = new KafkaProducer<>(props);
        ProducerRecord<String, String> record = new ProducerRecord<String, String>(topic, "message", input);
        producer.send(record);
        producer.close(); // close to free up resources

        //TODO: this method always return OK, even if send method has failed
        return new ResponseEntity<>("Message Insertion OK:" + input, HttpStatus.OK);
        }
}