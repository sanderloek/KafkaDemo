package com.example.demo.client;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.common.KafkaFuture;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class KafkaAdminClient {

    Properties props;

    public KafkaAdminClient()
    {

        props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
    }
    public ResponseEntity createKafkaTopic() {

        try (Admin admin = Admin.create(props)) {
            String topicName = "my-topic-random";
            int partitions = 1;
            short replicationFactor = 1;

            CreateTopicsResult result = admin.createTopics(Collections.singleton(
                    new NewTopic(topicName, partitions, replicationFactor)));


            return new ResponseEntity<>("Topic created successfully " + result.values().get(topicName).get(), HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            return new ResponseEntity<>("Topic creation failed " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    public ResponseEntity listKafkaTopics() {
        try (Admin admin = Admin.create(props)) {

            ListTopicsResult result = admin.listTopics();


            return new ResponseEntity<>(result.names().get(), HttpStatus.OK);

        } catch (ExecutionException | InterruptedException e) {
            return new ResponseEntity<>("Topic listing failed " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }


    }
}
