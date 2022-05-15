package com.example.demo.client;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

public class KafkaAdminClient {

    private final Properties props;

    public KafkaAdminClient() {
        props = new Properties();
        props.put("bootstrap.servers", "localhost:9092");
        // TODO: read value value from app.properties
    }

    public ResponseEntity createKafkaTopic(String input) {

        try (Admin admin = Admin.create(props)) {
            int partitions = 1;
            short replicationFactor = 1;

            CreateTopicsResult result = admin.createTopics(Collections.singleton(
                    new NewTopic(input, partitions, replicationFactor)));

            result.all().get(); // this call to make sure topic is created
            return new ResponseEntity<>("Topic created successfully", HttpStatus.OK);
        } catch (ExecutionException | InterruptedException e) {
            return new ResponseEntity<>("Topic creation failed " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity listKafkaTopics() {
        try (Admin admin = Admin.create(props)) {

            ListTopicsResult result = admin.listTopics();
            return new ResponseEntity<>(result.names().get(), HttpStatus.OK);

        } catch (ExecutionException | InterruptedException e) { //exception thrown if .get fails
            return new ResponseEntity<>("Topic listing failed " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
}
