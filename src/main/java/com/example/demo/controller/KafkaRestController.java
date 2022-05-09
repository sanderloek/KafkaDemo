package com.example.demo.controller;

import com.example.demo.client.KafkaAdminClient;
import com.example.demo.client.KafkaProducerClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class KafkaRestController {

KafkaAdminClient kafkaAdminClient = new KafkaAdminClient();
KafkaProducerClient kafkaProducerClient = new KafkaProducerClient();

    @PostMapping("/api/v1/topics")
    public ResponseEntity processCreateKafkaTopic() {
       return kafkaAdminClient.createKafkaTopic();

    }

    @GetMapping("/api/v1/topics")
    public ResponseEntity processListKafkaTopic() {
        return kafkaAdminClient.listKafkaTopics();

    }

    @PostMapping("/api/v1/topics/{topic}")
    public ResponseEntity processInsertMessage(@PathVariable String topic, @RequestBody String input) {
        return kafkaProducerClient.insertMessage(topic, input);

    }

    @GetMapping("/api/v1/topics/{topic}")
    public ResponseEntity processReadMessage(@PathVariable String topic, @RequestBody String input) {
        return kafkaConsumerClient.readMessage(topic, input);

    }


}

