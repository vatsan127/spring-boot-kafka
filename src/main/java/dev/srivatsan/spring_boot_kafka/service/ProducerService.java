package dev.srivatsan.spring_boot_kafka.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
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
            String message = "Hello World - " + LocalDateTime.now();
            sendMessage(message);
            TimeUnit.SECONDS.sleep(2);
        }
    }

    public void sendMessage(String message) {
        log.info("Producer Sending message: {}", message);
        kafkaTemplate.send(topic, message);
    }

}