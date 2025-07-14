package dev.srivatsan.spring_boot_kafka.service;

import dev.srivatsan.spring_boot_kafka.dto.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ConsumerService {

    @KafkaListener(topics = "${spring.kafka.consumer.topic}")
    public void consume(Employee message) {
        log.info("Received message: {}", message);
    }

}
