package dev.srivatsan.spring_boot_kafka.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class ProducerService {

    @Value("${spring.kafka.producer.topic}")
    private String topic;

    private KafkaTemplate<String, String> kafkaTemplate;

    public ProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Async
    public void sendMessageInfinitely() throws InterruptedException {
        while (true){
            sendMessage();
            TimeUnit.SECONDS.sleep(2);
        }
    }

    public void sendMessage() {
        kafkaTemplate.send(topic, "Hello World - " + LocalDateTime.now());
    }

}