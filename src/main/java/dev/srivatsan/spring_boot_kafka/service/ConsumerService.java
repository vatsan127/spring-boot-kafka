package dev.srivatsan.spring_boot_kafka.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ConsumerService {

    @KafkaListener(topics = "${spring.kafka.consumer.topic}")
    public void consume(String message) {
        log.info("Received message: {}", message);
    }

}
