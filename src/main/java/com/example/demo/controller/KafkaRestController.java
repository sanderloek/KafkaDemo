package com.example.demo.controller;

import com.example.demo.client.KafkaAdminClient;
import com.example.demo.client.KafkaConsumerClient;
import com.example.demo.client.KafkaProducerClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class KafkaRestController {

    KafkaAdminClient kafkaAdminClient = new KafkaAdminClient();
    KafkaProducerClient kafkaProducerClient = new KafkaProducerClient();
    KafkaConsumerClient kafkaConsumerClient = new KafkaConsumerClient();


    @PostMapping("/api/v1/topics")
    public ResponseEntity processCreateKafkaTopic(@RequestBody String input) {
        return kafkaAdminClient.createKafkaTopic(input);
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
    public ResponseEntity processReadMessage(@PathVariable String topic, @RequestParam int limit) {
        return kafkaConsumerClient.readMessage(topic, limit);
    }


}


